package edu.unm.entity;

/**
 * created by:
 * author: MichaelMillar
 */
//public class BallotBuilder {
//    // TODO: Build a blank ballot from the current election ballot schema
//}

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;

public class BallotBuilder {

    public static void buildBallotXML(Ballot ballot, String filePath) {
//        try {
//            JAXBContext context = JAXBContext.newInstance(Ballot.class);
//            Marshaller marshaller = context.createMarshaller();
//            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//
//            // Write to XML file
//            marshaller.marshal(ballot, new File(filePath));
//
//            System.out.println("Ballot XML file created successfully at: " + filePath);
//        } catch (JAXBException e) {
//            e.printStackTrace();
//            System.err.println("Error creating the Ballot XML file: " + e.getMessage());
//        }
    }
}
