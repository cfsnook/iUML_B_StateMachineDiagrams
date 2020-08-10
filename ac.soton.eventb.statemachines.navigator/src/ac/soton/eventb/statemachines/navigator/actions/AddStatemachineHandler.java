/*******************************************************************************
 * Copyright (c) 2020-2020 University of Southampton.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package ac.soton.eventb.statemachines.navigator.actions;

import ac.soton.eventb.emf.diagrams.Diagram;
import ac.soton.eventb.emf.diagrams.navigator.handler.AbstractAddDiagramHandler;
import ac.soton.eventb.statemachines.StatemachinesFactory;

/**
 * Command handler for adding a new statemachine to a suitable container (see extension point for enablement)
 * 
 * All we need to do is implement the method to create a new diagram.
 * 
 * @author cfsnook
 *
 */
public class AddStatemachineHandler extends AbstractAddDiagramHandler {

	@Override
	public Diagram createNewDiagram() {
		return StatemachinesFactory.eINSTANCE.createStatemachine();
	}
	
}
