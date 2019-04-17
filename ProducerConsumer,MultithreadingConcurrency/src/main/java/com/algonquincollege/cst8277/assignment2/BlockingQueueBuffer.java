/**********************************************************************w***a******l******r*******us***********
 * File: BlockingQueueBuffer.java
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
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Description: Implements the {@link com.algonquincollege.cst8277.assignment2.Buffer} interface using {@link ArrayBlockingQueue}
 *
 * @date  (modified) 2019 02 24
 *
 * @author I. M. Student 040-88-4087, 040-88-3729
 * 
 *by:- Vaibhav Jain, Shadi Al Khalil
 * @param <E> the element type held in the buffer
 */
public class BlockingQueueBuffer<E> implements Buffer<E> {
    protected BlockingQueue<E> queue;
    protected int capacity;
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());// logger to track the execution


    /**
     * Constructor builds a buffer of the specified size
     * @param capacity
     */
    public BlockingQueueBuffer(int capacity) {
        // instantiating queue of type ArrayBlockingQueue
        queue = new ArrayBlockingQueue<>(capacity);
    }

    /**
     * Add element to buffer (thread-safe); if no room, block
     * @param element
     */
    public void blockingPut(E element) throws InterruptedException {
        // using blockingqueue method put in order to add elements to the buffer
        logger.info("blockingPut: The value of "+ queue.size() +" is "+ element );
        queue.put(element);
        
    }

    /**
     * Remove element from buffer (thread-safe); if none, block
     *
     * @return element
     */
    public E blockingGet() throws InterruptedException {
        // using blockingqueue method take in order to consume elements to the buffer
        if(!queue.isEmpty()) {
        logger.info("blockingGet: The value of "+ queue.size() +" is "+ queue.peek() );}
        return queue.take();
    }
}
