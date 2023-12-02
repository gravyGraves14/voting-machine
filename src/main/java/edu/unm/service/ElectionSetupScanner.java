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

    public static void main(String[] args) {
        ElectionSetupScanner scanner = new ElectionSetupScanner("test-schema.xml");
        try {
            scanner.parseSchema();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
                QuestionType type = QuestionType.of(qElement.getAttribute("type"));
                String summary = qElement.getElementsByTagName("summary").item(0).getTextContent();
                String question = qElement.getElementsByTagName("question_text").item(0).getTextContent();
                List<QuestionOption> options = new ArrayList<>();

                NodeList optionNodeList = qElement.getElementsByTagName("option");
                for (int j = 0; j < optionNodeList.getLength(); j++) {
                    Node oNode = optionNodeList.item(j);

                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element oElement = (Element) oNode;

                        String oText = oElement.getElementsByTagName("option_text").item(0).getTextContent();
                        String affilText = oElement.getElementsByTagName("affiliation").item(0).getTextContent();

                        options.add(new QuestionOption(oText, affilText));
                    } else {
                        throw INVALID_PARSE_EXCEPTION;
                    }
                }

                BallotQuestion ballotQuestion = new BallotQuestion(summary, question, type, options);
                questions.add(ballotQuestion);
            } else {
                throw INVALID_PARSE_EXCEPTION;
            }
        }
        return schemaBallot;
    }
}
