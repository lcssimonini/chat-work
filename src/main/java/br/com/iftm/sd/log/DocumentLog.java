package br.com.iftm.sd.log;

import br.com.iftm.sd.client.ClientInformation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;

public class DocumentLog {

    private static final String MESSAGE_TEMPLATE = "<%s>@<%s>@<%s>#<%s>\n";
    private static final String FILE_PATH = "logs.txt";
	private Path path = Paths.get(FILE_PATH);

	public DocumentLog() {
        File logFile = new File(FILE_PATH);
        try {
            logFile.createNewFile();
        } catch (IOException ignored) {}
    }

	public void writeLog(Map<ClientInformation, List<String>> messages) {
	    messages.forEach((info, message) -> {
            try {
                writeLogMessage(info, message);
            } catch (IOException ignored) {}
        });
    }

    private void writeLogMessage(ClientInformation info, List<String> message) throws IOException {
        Files.write(path, getLogMessage(info, message), StandardOpenOption.APPEND);
    }

    private byte[] getLogMessage(ClientInformation info, List<String> message) {
        return String.format(MESSAGE_TEMPLATE, info.getHost(), info.getIpAddress(), info.getServerPort(), message).getBytes();
    }
}
