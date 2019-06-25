package tasks;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.nio.file.WatchEvent;

public class WatchFilesChange implements Runnable {
	Path myDir;
	WatchService watcher;
	PrintWriter printWriter;

	WatchFilesChange(String path) {
		try {
			myDir = Paths.get(path);
			watcher = myDir.getFileSystem().newWatchService();
			myDir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE,
					StandardWatchEventKinds.ENTRY_MODIFY);
			FileWriter fr = new FileWriter(new File(
					"/home/igornfaustino/Documents/code/faculdade/optativas/programacao_concorrente/tasks/log.txt"),
					true);
			BufferedWriter br = new BufferedWriter(fr);
			printWriter = new PrintWriter(br);
			printWriter.println("iniciando watcher..." + new Date().toString());
			printWriter.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {
		while (true) {
			try {
				WatchKey watchKey = watcher.take();
				List<WatchEvent<?>> events = watchKey.pollEvents();
				for (WatchEvent<?> event : events) {
					if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
						printWriter.println("Created: " + event.context().toString() + " >> " + new Date().toString());
					} else if (event.kind() == StandardWatchEventKinds.ENTRY_DELETE) {
						printWriter.println("Delete: " + event.context().toString() + " >> " + new Date().toString());
					} else if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
						printWriter.println("Modify: " + event.context().toString() + " >> " + new Date().toString());
					}
				}
				printWriter.flush();
				watchKey.reset();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		ExecutorService executor = Executors.newSingleThreadExecutor();

		executor.submit(new WatchFilesChange(
				"/home/igornfaustino/Documents/code/faculdade/optativas/programacao_concorrente/tasks/folder"));

	}
}