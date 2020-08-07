/*
 * Copyright (c) 2010 University of Southampton.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package ac.soton.eventb.statemachines.diagram.providers;

import org.eclipse.gmf.tooling.runtime.providers.DefaultEditPartProvider;

import ac.soton.eventb.statemachines.diagram.edit.parts.RootStatemachineEditPart;
import ac.soton.eventb.statemachines.diagram.edit.parts.StatemachinesEditPartFactory;
import ac.soton.eventb.statemachines.diagram.part.StatemachinesVisualIDRegistry;

/**
 * @generated
 */
public class StatemachinesEditPartProvider extends DefaultEditPartProvider {

	/**
	 * @generated
	 */
	public StatemachinesEditPartProvider() {
		super(new StatemachinesEditPartFactory(),
				StatemachinesVisualIDRegistry.TYPED_INSTANCE,
				RootStatemachineEditPart.MODEL_ID);
	}
}
