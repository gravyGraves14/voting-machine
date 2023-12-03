package edu.unm.dao;

import edu.unm.entity.*;
import edu.unm.service.ElectionSetupScanner;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.*;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal;

public class ElectionGremlinDAO implements ElectionDAO {

    public static void main(String[] args) {
        ElectionSetupScanner scanner = new ElectionSetupScanner("test-schema.xml");
        try {
            Ballot ballot = scanner.parseSchema();
            ElectionGremlinDAO dao = new ElectionGremlinDAO();
            dao.loadBallotSchema(ballot.getSchemaName(), ballot);

            ballot.getQuestions().forEach(q -> q.getOptions().get(1).setSelected(true));
            dao.saveBallotVotes(ballot);

            Ballot results = dao.getTabulation(ballot.getSchemaName());
            System.out.println("Results for schema: " + ballot.getSchemaName());
            for (BallotQuestion question : results.getQuestions()) {
                System.out.println("Question: " + question.getQuestion());
                for (QuestionOption option : question.getOptions()) {
                    System.out.println("\t" + option.getTotalVotes() + " - " + option.getOption());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Logger LOGGER = Logger.getLogger(getClass().getName());

    private Configuration config;
    private Graph graph;
    private GraphTraversalSource source;

    private BallotQuestion mapQuestion(Vertex vertex) {
        String summaryText = "";
        String fullText = "";
        QuestionType qType = null;
        Iterator<VertexProperty<Object>> props = vertex.properties(
                "summary", "full", "type");
        while (props.hasNext()) {
            VertexProperty<Object> p = props.next();
            String key = p.key();
            switch (key) {
                case "summary" -> summaryText = (String) p.value();
                case "full" -> fullText = (String) p.value();
                case "type" -> qType = QuestionType.of((String) p.value());
            }
        }
        return new BallotQuestion(summaryText, fullText, qType);
    }

    private QuestionOption mapOption(Vertex vertex) {
        String optionText = "";
        String affilText = "";

        Iterator<VertexProperty<Object>> props = vertex.properties("text", "affiliation");
        while(props.hasNext()) {
            VertexProperty<Object> p = props.next();
            String key = p.key();
            switch (key) {
                case "text" -> optionText = (String) p.value();
                case "affiliation" -> affilText = (String) p.value();
            }
        }

        return new QuestionOption(optionText, affilText);
    }

    public void loadBallotSchema(String schemaName, Ballot ballot) {
        connect();
        // Create schema vertex
        Map<Object, Object> props = new HashMap<>();
        props.put("name", schemaName);
        Vertex schemaVertex = saveVertex("schema", props);

        for (BallotQuestion question : ballot.getQuestions()) {
            props = new HashMap<>();
            props.put("summary", question.getQuestionShort());
            props.put("full", question.getQuestion());
            props.put("type", question.getType().toString());

            Vertex qVertex = saveVertex("question", props);

            Edge e1 = saveEdge("has_question", schemaVertex, qVertex, new HashMap<>());

            for (QuestionOption option : question.getOptions()) {
                props = new HashMap<>();
                props.put("text", option.getOption());
                props.put("affiliation", option.getAffiliation());

                Vertex oVertex = saveVertex("option", props);

                Edge e2 = saveEdge("has_option", qVertex, oVertex, new HashMap<>());

            }
        }
        close();
    }

    public void saveBallotVotes(Ballot ballot) {
        connect();
        String schema = ballot.getSchemaName();

        Map<Object, Object> props = new HashMap<>();
        props.put("ballotId", ballot.getBallotId());
        Vertex ballotVertex = saveVertex("ballot", props);

        for (BallotQuestion question : ballot.getQuestions()) {
            Optional<QuestionOption> oSelected = question.getSelectedOption();
            // nothing selected, continue
            if (oSelected.isEmpty()) {
                continue;
            }
            QuestionOption selected = oSelected.get();

            Optional<Vertex> ov = findQuestionVertexInSchema(schema, question.getQuestionShort());

            if (ov.isEmpty()) {
                LOGGER.severe("Unable to find matching ballot question. Canceling save.");
                return;
            }

            Vertex qv = ov.get();
            List<Vertex> optionVertices = findAllVerticesFromWithEdgeLabel("has_option", qv);
            Optional<Vertex> oVertex = optionVertices.stream()
                    .filter(v -> v.property("text")
                            .value().equals(selected.getOption()))
                    .findFirst();

            Vertex optionVertex;
            if (oVertex.isEmpty()) {
                Map<Object, Object> oProps = new HashMap<>();
                oProps.put("text", selected.getOption());
                optionVertex = saveVertex("option", oProps);
            } else {
                optionVertex = oVertex.get();
            }

            saveEdge("voted_for", optionVertex, ballotVertex, new HashMap<>());


        }


        close();
    }

    public Ballot getTabulation(String schema) {
        try {
            connect();

            List<BallotQuestion> questions = new ArrayList<>();
            Ballot ballot = new Ballot(schema, questions);

            GraphTraversal<Vertex, Vertex> traversal = source.V().hasLabel("schema").has("name", schema)
                    .out("has_question");
            while (traversal.hasNext()) {
                Vertex q = traversal.next();
                BallotQuestion question = mapQuestion(q);
                questions.add(question);

                GraphTraversal<Vertex, Vertex> oTraversal = source.V(q.id()).out("has_option");
                while (oTraversal.hasNext()) {
                    Vertex o = oTraversal.next();
                    QuestionOption option = mapOption(o);
                    question.addOption(option);

                    long votes = source.V(o.id()).outE("voted_for").count().next();
                    option.setTotalVotes(votes);
                }
            }

            return ballot;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            close();
        }

    }

    public Ballot getBallotFromSchema(String schema) {
        try {
            connect();

            List<BallotQuestion> questions = new ArrayList<>();
            Ballot ballot = new Ballot(schema, questions);

            GraphTraversal<Vertex, Vertex> traversal = source.V().hasLabel("schema").has("name", schema)
                    .out("has_question");
            while (traversal.hasNext()) {
                Vertex q = traversal.next();
                BallotQuestion question = mapQuestion(q);
                questions.add(question);

                GraphTraversal<Vertex, Vertex> oTraversal = source.V(q.id()).out("has_option");
                while (oTraversal.hasNext()) {
                    Vertex o = oTraversal.next();
                    QuestionOption option = mapOption(o);
                    question.addOption(option);
                }
            }

            return ballot;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            close();
        }
    }

    public Optional<Vertex> findQuestionVertexInSchema(String schema, String summary) {
        try {
            return Optional.of(source.V().hasLabel("schema").has("name", schema)
                    .out("has_question").hasLabel("question")
                    .has("summary", summary).next());
        } catch (NoSuchElementException e) {
            return Optional.empty();
        }
    }

    private void connect() {
        LOGGER.info("Connecting to Gremlin DB");
        try {
            if (config == null) {
                loadConfiguration();
            }
            graph = TinkerGraph.open(config);
            source = traversal().withEmbedded(graph);
        } catch (Exception e) {
            LOGGER.severe("Failed to connect to Gremlin DB");
            e.printStackTrace();
        }
    }

    private void close() {
        LOGGER.info("Closing Gremlin DB");
        try {
            graph.close();
        } catch (Exception e) {
            LOGGER.severe("Failed to close Gremlin DB");
            e.printStackTrace();
        }
    }

    public Vertex saveVertex(String label, Map<Object, Object> vMap) {
        LOGGER.log(Level.INFO, "Saving new vertex with label: " + label);
        try {
//            connect();
            return source.addV(label).property(vMap).next();
        } finally {
//            close();
        }
    }

    public Optional<Vertex> findVertex(Vertex vertex) {
        return findVertexById(vertex.id());
    }

    public Optional<Vertex> findVertexById(Object id) {
        try {
            Vertex v = source.V(id).next();
            return Optional.of(v);
        } catch (NoSuchElementException e) {
            LOGGER.warning("Failed to find vertex by id: " + id);
            return Optional.empty();
        }
    }

    public Optional<Vertex> findFirstVertexByProperty(String key, Object value) {
        try {
            return Optional.of(source.V().has(key, value).next());
        } catch (NoSuchElementException e) {
            LOGGER.log(Level.WARNING, "Failed to find vertex by key/value: " + key + "=" + value);
            return Optional.empty();
        }
    }

    public Optional<Vertex> findVertexByLabelWithProperty(String label, String key, Object value) {
        try {
            return Optional.of(source.V().hasLabel(label).has(key, value).next());
        } catch (NoSuchElementException e) {
            return Optional.empty();
        }
    }

    public Optional<List<Vertex>> findAllVerticesByProperty(String key, Object value) {
        try {
            List<Vertex> vertices = source.V().has(key, value).toList();
            return Optional.of(vertices);
        } catch (NoSuchElementException e) {
            LOGGER.log(Level.WARNING, "Failed to find vertices by key/value: " + key + "=" + value);
            return Optional.empty();
        }
    }

    public List<Vertex> findAllVerticesByLabel(String label) {
        try {
            return source.V().hasLabel(label).toList();
        } catch (NoSuchElementException e) {
            LOGGER.log(Level.WARNING, "Failed to find vertices with label: " + label);
            return new ArrayList<>();
        }
    }

    public List<Vertex> findAllVerticesFromWithEdgeLabel(String edgeLabel, Vertex from) {
        try {
            List<Vertex> vertices = new ArrayList<>();
            Iterator<Edge> edges = from.edges(Direction.from, edgeLabel);
            while (edges.hasNext()) {
                Edge e = edges.next();
                Vertex v = e.inVertex();
                vertices.add(v);
            }
            return vertices;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public Edge saveEdge(String label, Vertex from, Vertex to, Map<Object, Object> properties) {
        LOGGER.log(Level.INFO, "Saving edge from: " + from +
                ", to: " + to + ", with label: " + label);
        try {
//            connect();
            return source.addE(label).from(from).to(to).property(properties).next();
        } finally {
            LOGGER.log(Level.INFO, "Saving and reloading GremlinService from saveEdge.");
//            close();
        }
    }

    public Optional<Edge> findEdge(Edge edge) {
        return findEdgeById((Integer) edge.id());
    }

    public Optional<Edge> findEdgeById(Integer id) {
        try {
            return Optional.of(source.E(id).next());
        } catch (NoSuchElementException e) {
            LOGGER.log(Level.INFO, "Failed to find edge by id: " + id);
            return Optional.empty();
        }
    }

    public Optional<List<Edge>> findAllEdges() {
        try {
            return Optional.of(source.E().toList());
        } catch (NoSuchElementException e) {
            LOGGER.log(Level.SEVERE, "Failed to find ANY edges");
            return Optional.empty();
        }
    }

    public Optional<List<Edge>> findAllEdgesByLabel(String label) {
        try {
            return Optional.of(source.E().hasLabel(label).toList());
        } catch (NoSuchElementException e) {
            LOGGER.log(Level.WARNING, "Failed to find any edges by label: " + label);
            return Optional.empty();
        }
    }

    public Optional<List<Edge>> findAllEdgesFromVertex(Vertex vertex) {
        try {
            return Optional.of(source.V(vertex.id()).outE().toList());
        } catch (NoSuchElementException e) {
            LOGGER.log(Level.WARNING, "Failed to find any edges from vertex: " + vertex);
            return Optional.empty();
        }
    }

    public Optional<List<Edge>> findAllEdgesToVertex(Vertex vertex) {
        try {
            return Optional.of(source.V(vertex.id()).inE().toList());
        } catch (NoSuchElementException e) {
            LOGGER.log(Level.WARNING, "Failed to find any edges to vertex: " + vertex);
            return Optional.empty();
        }
    }

    public Optional<List<Edge>> findAllEdgesFromVertexByLabel(Vertex vertex, String label) {
        try {
            return Optional.of(source.V(vertex.id()).outE(label).toList());
        } catch (NoSuchElementException e) {
            LOGGER.log(Level.WARNING, "Failed to find any edges from vertex: " + vertex +
                    ", with label: " + label);
            return Optional.empty();
        }
    }

    public Optional<List<Edge>> findAllEdgesToVertexByLabel(Vertex vertex, String label) {
        try {
            return Optional.of(source.V(vertex.id()).inE(label).toList());
        } catch (NoSuchElementException e) {
            LOGGER.log(Level.WARNING, "Failed to find any edges to vertex: " + vertex +
                    ", with label: " + label);
            return Optional.empty();
        }
    }

    @Override
    public void commit() {
        // dont think this is needed
    }

    @Override
    public void rollback() {
        // dont think this is needed
    }

    private void loadConfiguration() {
        config = new PropertiesConfiguration();
        config.setProperty("gremlin.tinkergraph.graphLocation", "election-gdb");
        config.setProperty("gremlin.tinkergraph.graphFormat", "graphml");
        config.setProperty("gremlin.tinkergraph.vertexIdManager", "INTEGER");
        config.setProperty("gremlin.tinkergraph.edgeIdManager", "INTEGER");
        config.setProperty("gremlin.tinkergraph.vertexPropertyIdManager", "INTEGER");
        config.setProperty("gremlin.tinkergraph.defaultVertexPropertyCardinality", "list");
    }
}
