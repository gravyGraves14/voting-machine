package edu.unm.service;

import edu.unm.entity.Ballot;
import edu.unm.entity.BallotQuestion;
import edu.unm.entity.QuestionOption;
import edu.unm.entity.QuestionType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * created by:
 * author: MichaelMillar
 */
public class ElectionSetupScanner {

    private static final IOException INVALID_PARSE_EXCEPTION =
            new IOException("Unable to parse question node, invalid node type");

    private final String filename;

    public ElectionSetupScanner(String filename) {
        this.filename = filename;
    }

    /**
     * Parses a ballot schema into the database in the follow format:
     * <ballotSchema>
     * 	<question minSelections=1 maxSelections=1 type="candidate">
     * 		<summary>President</summary>
     * 		<text>President of the United States of America</text>
     * 		<options>
     * 			<option>George Lucas</option>
     * 			<option>Wiley Coyote</option>
     * 		</options>
     * 	</question>
     * 	...
     * </ballotSchema>
     *
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public Ballot parseSchema() throws IOException, SAXException, ParserConfigurationException {
        File inputFile = new File(filename);

        // build document
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(inputFile);
        doc.getDocumentElement().normalize();

        // Build ballot
        List<BallotQuestion> questions = new ArrayList<>();
        Ballot schemaBallot = new Ballot(questions);

        // get all question elements
        NodeList questionNodes = doc.getElementsByTagName("question");
        // iterate through all questions and build schema
        for (int i = 0; i < questionNodes.getLength(); i++) {
            Node node = questionNodes.item(i);

            // verify node type
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element qElement = (Element) node;

                // build question data
                int minSelections = Integer.parseInt(qElement.getAttribute("minSelections"));
                int maxSelections = Integer.parseInt(qElement.getAttribute("maxSelections"));
                QuestionType type = QuestionType.of(qElement.getAttribute("type"));
                String summary = "";
                String question = "";
                List<QuestionOption> options = new ArrayList<>();

                // iterate through all sub attributes
                NodeList qChildrenList = qElement.getChildNodes();
                for (int j = 0; j < qChildrenList.getLength(); j++) {
                    Node subNode = qChildrenList.item(j);

                    if (subNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element cElement = (Element) subNode;
                        String cName = cElement.getNodeName();

                        switch (cName) {
                            case "summary" -> summary = cElement.getTextContent();
                            case "text" -> question = cElement.getTextContent();
                            case "options" -> {
                                NodeList oChildrenList = cElement.getChildNodes();
                                for (int k = 0; k < oChildrenList.getLength(); k++) {
                                    Node oNode = oChildrenList.item(k);

                                    if (oNode.getNodeType() == Node.ELEMENT_NODE) {
                                        Element oElement = (Element) oNode;
                                        options.add(new QuestionOption(oElement.getTextContent()));
                                    } else {
                                        throw INVALID_PARSE_EXCEPTION;
                                    }
                                }
                            }

                        }
                    } else {
                        throw INVALID_PARSE_EXCEPTION;
                    }
                }

                if (summary.isEmpty() || question.isEmpty() || options.size() == 0) {
                    throw new IOException("Invalid question parse, missing data.");
                }

                BallotQuestion ballotQuestion = new BallotQuestion(summary, question,
                        type, minSelections, maxSelections, options);
                questions.add(ballotQuestion);
            } else {
                throw INVALID_PARSE_EXCEPTION;
            }
        }
        return schemaBallot;
    }
}
