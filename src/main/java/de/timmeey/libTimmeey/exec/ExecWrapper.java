package de.timmeey.libTimmeey.exec;

import java.io.IOException;
import java.io.PipedOutputStream;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;

public class ExecWrapper {

    public ExecResultWrapper exec(final String command) throws IOException {
        PipedOutputStream stdOut = new PipedOutputStream();
        PipedOutputStream stdErr = new PipedOutputStream();
        CollectingOutputStream outputStream = new CollectingOutputStream
            (stdOut);
        CollectingOutputStream errorStream = new CollectingOutputStream(stdErr);
        CommandLine commandline = CommandLine.parse(command);
        DefaultExecutor exec = new DefaultExecutor();
        DefaultOwnExecuteResultHandler resultHandler = new
            DefaultOwnExecuteResultHandler();
        PumpStreamHandler streamHandler = new PumpStreamHandler(stdOut, stdErr);
        exec.setStreamHandler(streamHandler);
        ExecuteWatchdog watchdog = new ExecuteWatchdog(ExecuteWatchdog
            .INFINITE_TIMEOUT);
        exec.setWatchdog(watchdog);
        exec.setExitValue(0);
        exec.execute(commandline, resultHandler);
        return new ExecResultWrapper(outputStream, errorStream, commandline.toString(), resultHandler, watchdog);
    }

	public class DefaultOwnExecuteResultHandler extends DefaultExecuteResultHandler {
		private long endTime;
//		private final CollectingOutputStream stdOut;
//		private final CollectingOutputStream stdErr;
//
//		public DefaultOwnExecuteResultHandler(CollectingOutputStream stdOut, CollectingOutputStream stdErr) {
//			super();
//			this.stdOut = stdOut;
//			this.stdErr = stdErr;
//		}



		@Override
		public void onProcessComplete(int exitValue) {
//			try {
//				stdErr.close();
//				stdOut.close();
//
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			this.endTime = System.currentTimeMillis();
			super.onProcessComplete(exitValue);
		}

		@Override
		public void onProcessFailed(ExecuteException e) {
//			try {
//				stdErr.close();
//				stdOut.close();
//
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			this.endTime = System.currentTimeMillis();
			super.onProcessFailed(e);
		}

        public long getEndTime(){
			return this.endTime;
		}

	}
}
