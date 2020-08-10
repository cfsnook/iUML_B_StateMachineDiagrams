/*******************************************************************************
 *  Copyright (c) 2010-2019 University of Southampton.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *   
 *  Contributors:
 *  University of Southampton - Initial implementation
 *******************************************************************************/
package ac.soton.eventb.statemachines.generator.enumRules;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eventb.emf.core.EventBNamedCommentedComponentElement;
import org.eventb.emf.core.context.Context;
import org.eventb.emf.core.machine.Machine;

import ac.soton.emf.translator.TranslationDescriptor;
import ac.soton.emf.translator.configuration.IRule;
import ac.soton.emf.translator.eventb.rules.AbstractEventBGeneratorRule;
import ac.soton.emf.translator.eventb.utils.Find;
import ac.soton.emf.translator.eventb.utils.Make;
import ac.soton.eventb.statemachines.Statemachine;
import ac.soton.eventb.statemachines.TranslationKind;
import ac.soton.eventb.statemachines.generator.strings.Strings;
import ac.soton.eventb.statemachines.generator.utils.Utils;

public class RootStatemachine2NewContextRule extends AbstractEventBGeneratorRule implements IRule{
	
	@Override
	public boolean enabled(EObject sourceElement) throws Exception  {	
		return Utils.isRootStatemachine((Statemachine)sourceElement) &&
				((Statemachine) sourceElement).getTranslation().equals(TranslationKind.SINGLEVAR); 
	}
	
	/**
	 * Generates the implicit context
	 */
	@Override
	public List<TranslationDescriptor> fire(EObject sourceElement, List<TranslationDescriptor> generatedElements) throws Exception {
		EventBNamedCommentedComponentElement container = Utils.getTranslationTarget();
		List<TranslationDescriptor> ret = new ArrayList<TranslationDescriptor>();
		Context implicitContext =  (Context) Make.context(Strings.CTX_NAME(container, (Statemachine)sourceElement), "");
		
		ret.add(Make.descriptor(Find.project(container), components, implicitContext ,1));
		ret.add(Make.descriptor(container, seesNames, implicitContext.getName(), 1));

		Statemachine refinedStatemachine = ((Statemachine)sourceElement).getRefines();
		if  (refinedStatemachine != null){
			EventBNamedCommentedComponentElement refinedContainer = ((Machine)Utils.getTranslationTarget()).getRefines().get(0);			
			if (refinedContainer != null){
				String refinedStatemachineContextName = Strings.CTX_NAME(refinedContainer, refinedStatemachine);
				if(!implicitContext.getExtendsNames().contains(refinedStatemachineContextName)){
					implicitContext.getExtendsNames().add(refinedStatemachineContextName);
				}
			}
		}
		return ret;
	}
	
}



	
	
	
