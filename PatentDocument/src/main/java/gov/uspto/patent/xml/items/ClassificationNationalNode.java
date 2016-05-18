package gov.uspto.patent.xml.items;

import java.text.ParseException;
import java.util.List;

import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.uspto.parser.dom4j.ItemReader;
import gov.uspto.patent.model.classification.Classification;
import gov.uspto.patent.model.classification.UspcClassification;

/*
	<classification-national>
		<country>US</country>
		<main-classification> 602031</main-classification>
	</classification-national>
*/

public class ClassificationNationalNode extends ItemReader<Classification>{
	private static final Logger LOGGER = LoggerFactory.getLogger(ClassificationNationalNode.class);

	private final static String ITEM_NODE_NAME = "classification-national";

	public ClassificationNationalNode(Node itemNode){
		super(itemNode, ITEM_NODE_NAME);
	}

	@Override
	public Classification read() {
		//Node countryNode = parentNode.selectSingleNode("country");
		Node mainClass = itemNode.selectSingleNode("main-classification");
		if (mainClass == null){
			return null;
		}

		UspcClassification classification;
		try {
			classification = UspcClassification.fromText(mainClass.getText());
			classification.setIsMainClassification(true);
		} catch (ParseException e1) {
			LOGGER.warn("Failed to parse USPC classification 'main-classification': {}", mainClass.asXML(), e1);
			return null;
		}

		@SuppressWarnings("unchecked")
		List<Node> furtherClasses = itemNode.selectNodes("further-classification");
		for (Node subclass: furtherClasses){

			try {
				UspcClassification usClass = UspcClassification.fromText(subclass.getText());
				classification.addChild( usClass );
			} catch (ParseException e) {
				LOGGER.warn("Failed to parse USPC classification 'further-classification': {}", subclass.asXML(), e);
			}

		}

		return classification;
	}
	
}
