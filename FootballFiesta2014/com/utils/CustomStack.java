/*
 * CustomStack.java
 *
 * © <your company here>, 2003-2008
 * Confidential and proprietary.
 */

package com.utils;

/**
 * Custom int Stack
 * @author Bikas
 */
public class CustomStack
{
    // The heart of this class. Contains objects of any type.
    private int[] stack;
    
    private static final int DEFAULT_CIRCULAR_STACK_CAPACITY = 5;
    
    // Capacity of this stack
    private int capacity;
    
    // Current length of this stack
    private int size;
    
    // Index of top element of this stack
    private int top;
    
    /**
    * No argument constructor. Initializes the stack with a capacity of 10 
    * elements.
    */
    public CustomStack()
    {
        capacity = DEFAULT_CIRCULAR_STACK_CAPACITY;
        stack = new int[capacity];
        size = 0;
        top = -1;
    }
    
    /**
    * Constructor with the capacity specified.
    * 
    * @param capacity Capacity of the stack
    */
    public CustomStack(int capacity)
    {
        this.capacity = capacity;
        stack = new int[capacity];
        size = 0;
        top = -1;
    }
    
    /**
    * Tells whether the stack is empty or not.
    * 
    * @return true if the stack is empty, false otherwise.
    */
    public boolean isEmpty()
    {
        return size == 0;
    }
    
    /**
    * Pushes the passed item on this stack. If the stack is already full, the 
    * item is pushed, however, in a circular manner and the old value in that 
    * place is replaced with the new item.
    * 
    * @param obj Object to be pushed on the stack
    */
    public void push(int val)
    {
        // No check for if stack is full. Push anyway.
        // Get the new top
        top = (++top) % capacity;
        
        // Push the new element
        stack[top] = val;
        
        // In case stack is full, this push operation does not increase the length.
        if (size < capacity)
        {
            size++;
        }
    }
  
    
    /**
    * Returns the top element on stack and remove it from the stack.
    * 
    * @return the top object on stack or -1 if the stack is empty.
    */
    public int pop()
    {
        if (size > 0)
        {
            // Get the top element
            int topItem = stack[top];
            
            // Remove the top element
            //stack[top] = null;
            
            // Adjust top and size
            top = (--top + capacity) % capacity;
            --size;
            
            return topItem;
        }
        else
        {
            return -1;
        }
    }
    
    /**
    * Returns the top element on stack 
    * 
    * @return the top object on stack or -1 if the stack is empty.
    */
    public int top()
    {
        if (size > 0)
        {
            // Get the top element
            int topItem = stack[top];
            return topItem;
        }
        else
        {
            return -1;
        }
    }
    
    
    
    /**
    * Returns how many items this stack currently has.
    * 
    * @return Size of the stack
    */
    public int getSize()
    {
        return size;
    }  
}   
