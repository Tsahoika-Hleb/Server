package com.lab3;

import com.lab3.DBModels.Student;
import com.lab3.DBModels.Subject;
import com.lab3.DBModels.SubjectScore;

import java.io.FileWriter;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;

public class Server {
    public static final String UNIQUE_BINDING_NAME = "StudentService";

    public static void main(String[] args) throws RemoteException, AlreadyBoundException, InterruptedException, SQLException {
        final Registry registry = LocateRegistry.createRegistry(7788);
        registry.rebind(UNIQUE_BINDING_NAME, new DatabaseManager());
        while (true) {
            Thread.sleep(Integer.MAX_VALUE);
        }
    }
}
