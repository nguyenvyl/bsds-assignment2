/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.programmerscuriosity.client;

import com.programmerscuriosity.model.RFIDLiftData;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.UUID;
/**
 *
 * @author nguyenvyl
 */
public class ReadData {
    public static ArrayList<RFIDLiftData> readData(String fileName) throws FileNotFoundException, IOException {
        ArrayList<RFIDLiftData> data = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(fileName)));
        String line;
        bufferedReader.readLine();
        while((line = bufferedReader.readLine()) != null) {
            line = bufferedReader.readLine();
            String[] entries = line.split(",");
            int resortID = Integer.parseInt(entries[0]);
            int dayNum = Integer.parseInt(entries[1]);
            int skierID = Integer.parseInt(entries[2]);
            int liftID = Integer.parseInt(entries[3]);
            int time = Integer.parseInt(entries[4]);
            RFIDLiftData rfidLiftData = new RFIDLiftData(resortID, dayNum, skierID, liftID, time);
            data.add(rfidLiftData);
        }
        return data;
    }
}
