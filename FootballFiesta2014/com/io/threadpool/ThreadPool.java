/*
 * ThreadPool.java
 * @author Bikas
 */

package  com.io.threadpool;

import java.util.Vector;

/**
 * Java Thread Pool
 *
 * This is a thread pool that for Java, it is
 * simple to use and gets the job done. This program and
 * all supporting files are distributed under the Limited
 * GNU Public License (LGPL, http://www.gnu.org).
 *
 * This is the main class for the thread pool. You should
 * create an instance of this class and assign tasks to it.
 *
 * For more information visit http://www.jeffheaton.com.
 *
 * @author Bikas 
 */

public class ThreadPool
{
    private static ThreadPool m_threadPoolSingleInstance = null;
    
    /* Array of worker threads that takes the runnable objects from the assignment list and executes them */
    private Thread threads[] = null;
    
    /* holds the list of runnable objects waiting to be executed */
    private Vector assignments = new Vector(3);
    
    /**
     * A Done object that is used to track when the
     * thread pool is done, that is has no more work
     * to perform.
     */
    protected Done done = new Done();
    
    private ThreadPool(int size)
    {
        threads = new WorkerThread[size];
        for (int iIndex = 0; iIndex < threads.length; iIndex++)
        {
            threads[iIndex] = new WorkerThread(this);
            threads[iIndex].start();
        }
    }
    
    public static void createSingleInstance(int size)
    {
        if (m_threadPoolSingleInstance == null)
        {
            m_threadPoolSingleInstance = new ThreadPool(size);
        }
    }
    
    public static ThreadPool getInstance() throws NullPointerException
    {
        if (m_threadPoolSingleInstance == null)
            throw new NullPointerException("Thread Pool not created.");
        return m_threadPoolSingleInstance;
    }
    
    public synchronized void addTask(Runnable r)
    {
        done.workerBegin();
        //  assigbments.push();
        assignments.addElement(r);
        //  assignments.add(r);
        notify();
    }
    
    public int countActiveThread()
    {
        return done.countActiveThred();
    }
    
    public void removeAllAssignments()
    {
        assignments.removeAllElements();
    }
    
    public synchronized Runnable getTask()
    {
        try
        {

            /*
            while ( !assignments.iterator().hasNext() )
            wait();
 
            Runnable r = (Runnable)assignments.iterator().next();
            assignments.remove(r);
            return r;
            */
            
            while(assignments.size() <= 0) wait();
            
            Runnable r = (Runnable)assignments.elementAt(0);
            assignments.removeElementAt(0);
            return r;
        }
        catch (Exception e)
        {
            done.workerEnd();
            return null;
        }
    }
    
    public void complete()
    {
        done.waitBegin();
        done.waitDone();
    }
    
    protected void finalize()
    {
        done.reset();
        for (int iIndex = 0; iIndex < threads.length; iIndex++)
        {
            threads[iIndex].interrupt();
            done.workerBegin();
            threads[iIndex] = null;
            //   threads[i].destroy();
        }
        done.waitDone();
    }
}

class WorkerThread extends Thread
{
    public boolean busy;
    public ThreadPool owner;
    
    WorkerThread(ThreadPool o)
    {
        owner = o;
    }
    
    public void run()
    {
        Runnable target = null;
        
        do
        {
            target = owner.getTask();
            if (target != null)
            {
                target.run();
                owner.done.workerEnd();
            }
        } while (target != null);
    }
}
