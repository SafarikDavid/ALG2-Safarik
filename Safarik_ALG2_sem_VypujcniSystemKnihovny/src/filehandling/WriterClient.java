/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filehandling;

import App.Client;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author David
 */
public abstract class WriterClient {
    public abstract void save(String path, List<Client> clientsList) throws IOException;
}
