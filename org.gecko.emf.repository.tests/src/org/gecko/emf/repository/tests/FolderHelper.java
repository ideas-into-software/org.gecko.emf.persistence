/**
 * Copyright (c) 2014 Data In Motion and others.
 * All rights reserved. 
 * 
 * This program and the accompanying materials are made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Data In Motion - initial API and implementation
 */
package org.gecko.emf.repository.tests;

import java.io.File;

/**
 * Helper class that handles folder creation and cleanup
 * @author Mark Hoffmann
 * @since 12.04.2015
 */
public class FolderHelper {
  
  private String baseFolder;
  private String folderName;
  private String folderPath;
  private File folderLocation;
  
  public FolderHelper(String folderName) {
    this(System.getProperty("java.io.tmpdir"), folderName);
  }
  
  public FolderHelper(String baseFolder, String folderName) {
    if (baseFolder == null) {
      baseFolder = System.getProperty("java.io.tmpdir");
    }
    this.baseFolder = baseFolder;
    if (folderName == null) {
      folderName = getClass().getSimpleName();
    }
    this.folderName = folderName;
  }
  
  /**
   * Initializes the folders
   */
  public void initialize() {
    baseFolder = baseFolder.replace('\\', '/');
    if(baseFolder.endsWith("/")){
      baseFolder = baseFolder.substring(0, baseFolder.length() - 1);
    }
    folderPath = baseFolder;
    folderPath = folderPath + "/" + folderName;
    folderLocation = new File(folderPath);
    if (!folderLocation.exists()) {
      folderLocation.mkdirs();
    }
  }
  
  /**
   * Cleans up all folders 
   */
  public void dispose() {
    if (folderLocation != null && folderLocation.exists()) {
      for (File child : folderLocation.listFiles()) {
        if (child.exists()) {
          child.delete();
        }
      }
      folderLocation.delete();
    }
  }
  
  /**
   * Returns the folder path
   * @return the folder path
   */
  public String getFolderPath() {
    return folderPath;
  }
  
  /**
   * Returns the {@link File} presentation of the folder location
   * @return the {@link File} presentation of the folder location
   */
  public File getFolderPathFile() {
    return folderLocation;
  }

}
