/*******************************************************************************
 * Copyright (c) 2011 University of Southampton.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package ac.soton.eventb.statemachines.navigator.actions;

import java.util.Collections;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.workspace.AbstractEMFOperation;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eventb.core.IEventBRoot;
import org.eventb.emf.core.EventBElement;

import ac.soton.eventb.emf.core.extension.navigator.provider.ExtensionNavigatorItem;
import ac.soton.eventb.emf.diagrams.DiagramOwner;
import ac.soton.eventb.statemachines.Statemachine;
import ac.soton.eventb.statemachines.StatemachinesFactory;
import ac.soton.eventb.statemachines.navigator.StatemachinesNavigatorPlugin;

/**
 * Command handler for adding a new statemachine to machine root.
 * 
 * @author vitaly
 *
 */
public class AddStatemachineHandler extends AbstractHandler {

	// name validator
	static final IInputValidator nameValidator = new IInputValidator(){

		@Override
		public String isValid(String name) {
			if (name.trim().isEmpty())
				return "";
			return null;
		}
	};
	
	/**
	 * EMF command for adding a statemachine to a machine or other container file.
	 * 
	 * @author vitaly
	 *
	 */
	public class AddStatemachineCommand extends AbstractEMFOperation {

		private URI uri;
		private Statemachine statemachine;

		public AddStatemachineCommand(URI uri, Statemachine statemachine) {
			super(TransactionalEditingDomain.Factory.INSTANCE.createEditingDomain(), "Add Statemachine");
			this.uri = uri;
			this.statemachine = statemachine;
		}

		@Override
		protected IStatus doExecute(IProgressMonitor monitor, IAdaptable info)
				throws ExecutionException {
			monitor.beginTask("Creating statemachine", IProgressMonitor.UNKNOWN);
			
			TransactionalEditingDomain editingDomain = getEditingDomain();
			
			try {
				Resource resource = editingDomain.getResourceSet().getResource(uri, true);
				
				if (resource != null && resource.isLoaded()) {
					EventBElement container =  (EventBElement) resource.getContents().get(0);
					if (container instanceof DiagramOwner) {
						((DiagramOwner)container).getDiagrams().add(statemachine);
					}else {
						container.getExtensions().add(statemachine);
					}
					resource.save(Collections.emptyMap());
				}
			} catch (Exception e) {
				return new Status(Status.ERROR, StatemachinesNavigatorPlugin.PLUGIN_ID, "Failed to add statemachine", e);
			} finally {
				monitor.done();
			}
			return Status.OK_STATUS;
		}

	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelectionChecked(event);
		if (selection instanceof IStructuredSelection) {
			URI uri = getFileURI(((IStructuredSelection) selection).getFirstElement());
				if (uri != null) {
					InputDialog dialog = new InputDialog(Display.getCurrent().getActiveShell(), 
							"New Statemachine", 
							"Enter statemachine name: ",
							null, nameValidator);
					if (dialog.open() == InputDialog.CANCEL)
						return null;
					String name = dialog.getValue().trim();
					
					Statemachine statemachine = StatemachinesFactory.eINSTANCE.createStatemachine();
					statemachine.setName(name);
					try {
						AddStatemachineCommand command = new AddStatemachineCommand(uri, statemachine);
						if (command.canExecute())
							command.execute(new NullProgressMonitor(), null);
					} catch (Exception e) {
						StatemachinesNavigatorPlugin.getDefault().logError("Creating statemachine failed", e);
					}
				}
		}
		return null;
	}

	private URI getFileURI(Object element) {
		if (element instanceof IEventBRoot) {
			IFile file = ((IEventBRoot)element).getResource();
			if (file==null || !file.exists()) return null;
			return URI.createPlatformResourceURI(file.getFullPath().toOSString(), true);
		}else if (element instanceof ExtensionNavigatorItem) {
			if (((ExtensionNavigatorItem)element).getEObject() instanceof DiagramOwner) {
				return ((ExtensionNavigatorItem)element).getEObject().eResource().getURI();
			}
		}
		return null;
	}

	
}
