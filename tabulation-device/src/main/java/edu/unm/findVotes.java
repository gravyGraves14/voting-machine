package edu.unm;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.util.NoSuchElementException;

public class findVotes {

    private final TinkerGraph graph;
    private final GraphTraversalSource g;

    public findVotes() {
        this.graph = TinkerGraph.open();
        this.g = graph.traversal();
    }

    public void addVoter(String name, String dateOfBirth, String SSN) {
        graph.addVertex(
                "name", name,
                "dateOfBirth", dateOfBirth,
                "SSN", SSN
        );
    }

    public void addCandidate(String candidateName) {
        graph.addVertex("name", candidateName);
    }

    public void recordVote(String voterSSN, String candidateName) {
        System.out.println("Recording vote...");
        System.out.println("Recording vote for voter SSN: " + voterSSN + ", candidate with name: " + candidateName);
        try {
            g.V().has("SSN", voterSSN).as("voter")
                    .V().has("name", candidateName).as("candidate")
                    .select("voter").addE("votedFor").to("candidate").next();
            System.out.println("Vote Recorded Successfully");
        } catch (NoSuchElementException e) {
            System.out.println("Error recording vote: " + e.getMessage());
        }
    }

    public void printGraph() {
        g.V().valueMap().forEachRemaining(System.out::println);
    }


    public static void main(String[] args) {
        findVotes votingGraph = new findVotes();

        // Add voters
        votingGraph.addVoter("John Doe", "1990-01-01", "111-22-3333");
        votingGraph.addVoter("Jane Smith", "1985-05-15", "444-55-6666");

        // Add Candidates
        votingGraph.addCandidate("Mickey Mouse");
        votingGraph.addCandidate("IDK");
        // Print the graph
        votingGraph.printGraph();

        // Record votes
        votingGraph.recordVote("111-22-3333", "Mickey Mouse");
        votingGraph.recordVote("444-55-6666", "IDK");


    }


}
