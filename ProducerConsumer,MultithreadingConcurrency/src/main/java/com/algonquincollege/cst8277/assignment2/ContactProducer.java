/**********************************************************************w***a******l******r*******us***********
 * File: ContactProducer.java
 * Course materials (19W) CST 8277
 * @author (original) Mike Norman
 */
package com.algonquincollege.cst8277.assignment2;

import static com.algonquincollege.cst8277.assignment2.Utils.buildStreamFromScanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.algonquincollege.cst8277.assignment2.model.Contact;
/**
 *
 * <b>Description</b></br></br>
 * Implements the {@link com.algonquincollege.cst8277.assignment2.Producer} interface </br>
 *
 * @date  (modified) 2019 02 24
 *
 * @author I. M. Student 040-88-4087, 040-88-3729
 * 
 *by:- Vaibhav Jain, Shadi Al Khalil
 *
 */
public class ContactProducer implements Producer<Optional<Contact>> {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    @Override
    public Logger getLogger() {
        return logger;
    }

    protected static final String COMMA = ",";
    protected static final String SOMETHING_WENT_WRONG_READING_CSV_FILE = "something went wrong reading CSV file {}";
    protected static final String SOMETHING_WENT_WRONG_CREATING_CONTACT = "something went wrong creating contact or adding to buffer";

    protected Buffer<Optional<Contact>> threadSafeBuffer;
    protected int numProduced = 0;
    protected String csvPath; // path to CSV file containing Contacts

    public ContactProducer(Buffer<Optional<Contact>> threadSafeBuffer, String pathToCSV) {
        this.threadSafeBuffer = threadSafeBuffer;
        this.csvPath = pathToCSV;
    }

    /**
     * Iterate through the csv file, building {@link Contact}'s and adding them to the buffer
     *
     * @return int number of Contacts build from csv file
     */
    @Override
    public int produce() {
        int count = 0;
        try (Scanner scanner = new Scanner(new File(csvPath));
            Stream<String> csvStream = buildStreamFromScanner(scanner)) {
            List<Contact> newContacts = csvStream
                .skip(1) //skip header record
                .map(csvLine -> buildNewContacts(csvLine))
                .collect(Collectors.toList());
            //a marker for object buffer when the buffer is empty so that consumer(s) know to stop
            while(scanner.hasNext()) {
                System.out.printf("%-12d%-12%-12d%-12%-12d%-12%-12d%-12%", 
                        scanner.next(), scanner.next(), scanner.next(), scanner.next(), scanner.next(), scanner.next(), scanner.next(), scanner.next());
            }
            newContacts.forEach(newContact ->
            {
                try {
                    threadSafeBuffer.blockingPut(Optional.of(newContact));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            
            threadSafeBuffer.blockingPut(Optional.empty());
            count = newContacts.size();
        }
        catch (FileNotFoundException | InterruptedException e) {
            logger.error(SOMETHING_WENT_WRONG_READING_CSV_FILE,csvPath,e);
        }
        return count;
    }

    /**
     * Extract values from csvLine and build a Contact object
     * and add to the buffer
     *
     * @param csvLine
     * @return newly built Contact
     */
    protected Contact buildNewContacts(String csvLine) {
        Contact con = new Contact();
        String []s=csvLine.split(COMMA);
        con.setStreet(s[0]);
        con.setCity(s[1]);
        con.setState(s[2]);
        con.setPostal(s[3]);
        con.setCountry(s[4]);
        con.setFirstName(s[5]);
        con.setLastName(s[6]);
        con.setEmail(s[7]);
        
        return con;
    }
}