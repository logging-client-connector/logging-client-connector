package log.appender

import java.util.regex.Pattern

class LogCapture {
	List<String> messages = new LinkedList<>()
	boolean capturingLogs = false

	void addMessage(String message) {
		if (capturingLogs) {
			messages.add(message)
		}
	}

	void startCapturingLogs() {
		capturingLogs = true
	}

	void stopCapturingLogs() {
		capturingLogs = false
	}

	boolean contains(String message) {
		messages.stream().anyMatch { String msg ->
			msg.contains(message)
		}
	}

	boolean contains(Pattern pattern) {
		messages.stream().anyMatch { String msg ->
			pattern.matcher(msg).find()
		}
	}
}
