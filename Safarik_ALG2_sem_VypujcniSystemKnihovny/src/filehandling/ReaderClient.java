/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filehandling;

import App.Client;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author David
 */
public abstract class ReaderClient {
    
    public abstract ArrayList<Client> load(String path) throws IOException;
    
}
