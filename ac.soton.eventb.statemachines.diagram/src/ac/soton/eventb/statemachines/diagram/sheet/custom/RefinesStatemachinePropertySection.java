/*
 * Copyright (c) 2014-2019 University of Southampton.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package ac.soton.eventb.statemachines.diagram.sheet.custom;

import java.util.List;

import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eventb.emf.core.AbstractExtension;
import org.eventb.emf.core.machine.Machine;

import ac.soton.eventb.emf.diagrams.sheet.AbstractEnumerationPropertySection;
import ac.soton.eventb.statemachines.Statemachine;
import ac.soton.eventb.statemachines.StatemachinesPackage;

/**
 * Refines property section for Statemachine.
 * 
 *
 */
public class RefinesStatemachinePropertySection extends AbstractEnumerationPropertySection {
	
	@Override
	protected EStructuralFeature getFeature() {
		return StatemachinesPackage.Literals.STATEMACHINE__REFINES;
	}

	@Override
	protected String[] getEnumerationFeatureValues() {
		EList<EObject> statemachinesToRefine = getStatemachinesToRefine();
		String[] values = new String[statemachinesToRefine.size()];
		int i = 0;
		for (EObject sm : statemachinesToRefine) {
			values[i++] = sm == null ? ""
					: ((Statemachine) sm).getName();
		}
		return values;
	}

	@Override
	protected String getFeatureAsText() {
		Statemachine statemachine = ((Statemachine) eObject).getRefines();
		return statemachine == null ? "" 
				: ((Statemachine) statemachine).getName();
	}
	
	@Override
	protected String getLabelText() {
		return "Refines:";
	}

	@Override
	protected List<EObject> getAvailableDataElements(){
		return (List<EObject>)getStatemachinesToRefine();
	}
	
	
	private EList<EObject> getStatemachinesToRefine(){
		EList<EObject> ret = ECollections.newBasicEList();
		EObject container = getTranslationTarget();
		if (container instanceof Machine) {
			Machine machine = (Machine) container;
			EList<Machine> abstractMachines = machine.getRefines();
			for (Machine m : abstractMachines) {
				//add all contained statemachines
				ret.addAll(m.getAllContained(StatemachinesPackage.Literals.STATEMACHINE, true));
				//check Containment extensions for referenced statemachines
				// this is coded reflectively (for now), to avoid depending on the Containment plugin
				for (AbstractExtension ae : m.getExtensions()) {
					if (ae.eClass().getName().equals("Containment")) {
						EReference extensionRef = (EReference) ae.eClass().getEStructuralFeature("extension");
						Object ext = ae.eGet(extensionRef, true);
						if (ext instanceof Statemachine) {
							ret.add((EObject) ext);
						}
					}
				}
			}
		}
		return ret;
	}
	
}
