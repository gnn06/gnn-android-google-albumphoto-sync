package gnn.com.googlealbumdownloadappnougat.service;

import android.content.Context;

import com.google.gson.Gson;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

import gnn.com.googlealbumdownloadappnougat.service.WorkerResultStore;

@RunWith(PowerMockRunner.class)
public class WorkerResultStoreTest {

    @Rule
    public TemporaryFolder tmpFolder = new TemporaryFolder();

    @Test
    public void store_with_empty() throws IOException {
        Context context = mock(Context.class);
        File folder = tmpFolder.newFolder();
        when(context.getFilesDir()).thenReturn(folder);
        WorkerResultStore store = new WorkerResultStore(context);
        store.store(WorkerResultStore.State.SUCCESS);
        store.store(WorkerResultStore.State.FAILURE);

        dumpFile(folder.getAbsoluteFile() + "/work.json");
    }

    private void dumpFile(String file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
    }

    @Test
    public void read_previous_format_file() throws IOException {
        File folder = tmpFolder.newFolder();
        FileWriter writer = new FileWriter(new File(folder,"work.json"));
        writer.write("[{\"state\":\"SUCCESS\"},{\"state\":\"FAILURE\"}]");
        writer.close();
        dumpFile(folder.getAbsolutePath() + "/work.json");

        Context context = mock(Context.class);
        when(context.getFilesDir()).thenReturn(folder);

        WorkerResultStore store = new WorkerResultStore(context);
        store.store(WorkerResultStore.State.SUCCESS);
    }
}