package de.timmeey.libTimmeey.exec;

import de.timmeey.libTimmeey.exec.ExecWrapper.DefaultOwnExecuteResultHandler;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.apache.commons.exec.ExecuteWatchdog;

public class ExecResultWrapper implements Future<Integer> {

	private final CollectingOutputStream stdOut;
	private final CollectingOutputStream stdErr;
	private final String executedCommand;
	private final long timeStarted;
	private final DefaultOwnExecuteResultHandler resultHandler;
	private final ExecuteWatchdog watchdog;

    protected ExecResultWrapper(CollectingOutputStream stdOut, CollectingOutputStream stdErr, String executedCommand,
			DefaultOwnExecuteResultHandler resultHandler, ExecuteWatchdog watchdog) {
		super();
		this.stdOut = stdOut;
		this.stdErr = stdErr;
		this.executedCommand = executedCommand;
		this.resultHandler = resultHandler;
		this.watchdog = watchdog;
		this.timeStarted = System.currentTimeMillis();

	}


	public int getExitStatus(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		return this.get(timeout, unit);

	}
	public int getExitStatus() throws InterruptedException, ExecutionException {
		return this.get();

	}

	public String getExecutedCommand() {
		return executedCommand;
	}

	/**
	 * Gives the already available STDOut as a Stringline array.
	 * WARNING: Does not wait until execution is complete.
	 * Make sure to wait for getExitStatus() to return, if you want
	 * the complete output
	 * @return the up to now outputted output as a line array
	 */
	public List<String> getStdOutLines(){
		return stdOut.getLines();
	}

	/**
	 * Gives the already available STDErr as a Stringline array.
	 * WARNING: Does not wait until execution is complete.
	 * Make sure to wait for getExitStatus() to return, if you want
	 * the complete output
	 * @return the up to now outputted output as a line array
	 */
	public List<String> getStdErrLines(){
		return stdErr.getLines();
	}

	public long getCurrentRuntime(){
		if(resultHandler.hasResult()){
			return resultHandler.getEndTime()-this.getTimeStarted();
		}else{
			throw new IllegalStateException("Process has not yet returned");
		}
	}

    public long getTimeStarted() {
        return timeStarted;
    }

	public boolean cancel(boolean force) {
		if(!isDone()){
			if(force){
				watchdog.destroyProcess();
				return true;
			}
		}
		return false;
	}

    public boolean isCancelled() {
        return watchdog.killedProcess();
    }

    public boolean isDone() {
        return resultHandler.hasResult() || watchdog.killedProcess();
    }

	public Integer get() throws InterruptedException, ExecutionException {
			try {
				resultHandler.waitFor();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return resultHandler.getExitValue();

    }

	public Integer get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
			try {
				resultHandler.waitFor(unit.toMillis(timeout));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return resultHandler.getExitValue();

    }

	public InputStream getStdOutAsStream() throws IOException{
		return this.stdOut.getStream();
	}

}
