package gov.uspto.patent.sgml.fragments;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Node;

import gov.uspto.parser.dom4j.DOMFragmentReader;
import gov.uspto.patent.model.entity.Address;
import gov.uspto.patent.model.entity.Assignee;
import gov.uspto.patent.model.entity.Name;
import gov.uspto.patent.sgml.items.AddressNode;
import gov.uspto.patent.sgml.items.NameNode;

public class AssigneeNode extends DOMFragmentReader<List<Assignee>> {
	private static final String FRAGMENT_PATH = "/PATDOC/SDOBI/B700/B730";

	public AssigneeNode(Document document) {
		super(document);
	}

	@Override
	public List<Assignee> read() {
		List<Assignee> assigneeList = new ArrayList<Assignee>();

		@SuppressWarnings("unchecked")
		List<Node> assignees = document.selectNodes(FRAGMENT_PATH);
		for (Node assigneeN : assignees) {
			Node dataNode = assigneeN.selectSingleNode("B731/PARTY-US");

			Assignee assignee = readAssignee(dataNode);

			assigneeList.add(assignee);
		}

		return assigneeList;
	}

	public Assignee readAssignee(Node assigneeNode){
		Name name = new NameNode(assigneeNode).read();

		Address address = new AddressNode(assigneeNode).read();

		return new Assignee(name, address);
	}

}
