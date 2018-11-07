package com.adacore.adaintellij.project;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtilCore;
import org.jetbrains.annotations.NotNull;

import com.adacore.adaintellij.file.AdaBodyFileType;
import com.adacore.adaintellij.file.AdaSpecFileType;
import com.adacore.adaintellij.file.GPRFileType;

/**
 * Project component somewhat representing the Ada-IntelliJ side of a project.
 * When a project is being loaded, this component determines whether or not
 * that project is considered to be an Ada project, and provides the
 * `isAdaProject` method to the rest of the Ada-IntelliJ plugin to determine
 * whether features/operations should be enabled/performed in the context of
 * that project.
 * Therefore, despite the class name `AdaProject` and even though the doc
 * sometimes refers to "this Ada project", an instance of this class could
 * be a component of a project that is in fact not an Ada project.
 */
public final class AdaProject implements ProjectComponent {
	
	/**
	 * The project to which this component belongs.
	 */
	private Project project;
	
	/**
	 * Whether or not this project is considered to be an
	 * Ada project by the Ada-IntelliJ plugin.
	 */
	private boolean isAdaProject = false;
	
	/**
	 * Constructs a new AdaProject given a project.
	 *
	 * @param project The project to attach to the constructed Ada project.
	 */
	public AdaProject(Project project) { this.project = project; }
	
	/**
	 * @see com.intellij.openapi.components.NamedComponent#getComponentName()
	 */
	@Override
	@NotNull
	public String getComponentName() { return "com.adacore.adaintellij.project.AdaProject"; }
	
	/**
	 * @see com.intellij.openapi.components.ProjectComponent#projectOpened()
	 *
	 * Checks if the project is an Ada project. Currently, a project is
	 * considered to be an Ada project if it contains in its file hierarchy
	 * any Ada spec file, Ada body file or GPR file.
	 */
	@Override
	public void projectOpened() {
		
		String adsFileExtension = AdaSpecFileType.INSTANCE.getDefaultExtension();
		String adbFileExtension = AdaBodyFileType.INSTANCE.getDefaultExtension();
		String gprFileExtension = GPRFileType.INSTANCE.getDefaultExtension();
		
		VfsUtilCore.iterateChildrenRecursively(
			project.getBaseDir(),
			null,
			fileOrDir -> {
				
				if (!fileOrDir.isDirectory() && (
						adsFileExtension.equals(fileOrDir.getExtension()) ||
						adbFileExtension.equals(fileOrDir.getExtension()) ||
						gprFileExtension.equals(fileOrDir.getExtension())
					))
				{
					isAdaProject = true;
					return false;
				}
				
				return true;
				
			}
		);
		
	}
	
	/**
	 * Returns whether or not this project is an Ada project.
	 *
	 * @return Whether or not this project is an Ada project.
	 */
	public boolean isAdaProject() { return isAdaProject; }
	
}
