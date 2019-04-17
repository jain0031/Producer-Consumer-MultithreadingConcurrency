/**********************************************************************w***a******l******r*******us***********
 * File: CircularBuffer.java
 * Course materials (19W) CST 8277
 * @author (original) Mike Norman, derived from code by Deitel & Associates, Inc.
 *         (Notes: Fig. 23.18: CircularBuffer.java Synchronizing access to a shared bounded buffer)
 *
 * (C) Copyright 1992-2015 by Deitel & Associates, Inc. and
 * Pearson Education,Inc.
 * All Rights Reserved.
 *
 * DISCLAIMER: The authors and publisher of this book have used their
 * best efforts in preparing the book. These efforts include the
 * development, research, and testing of the theories and programs to determine their
 * effectiveness. The authors and publisher make no warranty of any kind,
 * expressed or implied, with regard to these programs or to the
 * documentation contained in these books. The authors and publisher
 * shall not be liable in any event for incidental or
 * consequential damages in connection with, or arising out of, the
 * furnishing, performance, or use of these programs.
 *
 *************************************************************************/
package com.algonquincollege.cst8277.assignment2;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Array;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import ch.qos.logback.classic.Logger;

/**
 *
 * Description: Implements the
 * {@link com.algonquincollege.cst8277.assignment2.Buffer} interface using a
 * CircularBuffer </br>
 *
 * @date  (modified) 2019 02 24
 *
 * @author I. M. Student 040-88-4087, 040-88-3729
 * 
 *by:- Vaibhav Jain, Shadi Al Khalil
 *
 * @param <E> the element type held in the buffer
 */
public class CircularBuffer<E> implements Buffer<E> {
    protected E[] bufArray;
    private int occupiedCells = 0; // count number of buffers used
    private int writeIndex = 0; // index of next element to write to
    private int readIndex = 0; // index of next element
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());// logger to track the execution

    /**
     * Constructor builds a buffer of the specified size
     * 
     * @param capacity
     */
    @SuppressWarnings("unchecked")
    public CircularBuffer(int capacity) {
        //instantiating the buffer with a certain capacity or size
        bufArray = (E[]) new Object[capacity];
    }

    @Override
    /**
     * Add element to buffer (thread-safe); if no room, block
     * 
     * @param element
     */
    public synchronized void blockingPut(E element) throws InterruptedException {
        while (occupiedCells == bufArray.length) {
            System.out.println("buffer is full");
            wait();
        }
        bufArray[writeIndex] = element;
        logger.info("blockingPut: The value of "+ writeIndex +" is "+ element );
        writeIndex = (writeIndex + 1) % bufArray.length;
        ++occupiedCells;
        notifyAll();
    }

    @Override
    /**
     * Remove element from buffer (thread-safe); if none, block
     *
     * @return element
     */
    public synchronized E blockingGet() throws InterruptedException {
        // checing if the buffer is empty, so, let the thread wait for the producer
        while (occupiedCells == 0) {
            System.out.println("Buffer is empty");
            wait();
        }
        // assigning the first value of the array which have index 0 to readValue. 
        E readValue = bufArray[readIndex];
        logger.info("BloackingGet: The value of "+ readIndex +" is "+ readValue );
        // moving in the buffer from index to another
        readIndex = (readIndex + 1) % bufArray.length;
        --occupiedCells;
        notifyAll();
        return readValue;
    }
}
