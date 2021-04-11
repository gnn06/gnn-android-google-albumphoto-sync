package gnn.com.googlealbumdownloadappnougat.service;

import android.content.Context;

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
import java.io.IOException;

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
//        WorkInfo info = new WorkInfo(
//                new UUID(123, 456),
//                WorkInfo.State.ENQUEUED,
//                new Data.Builder().build(),
//                new ArrayList<String>(),
//                new Data.Builder().build(),
//                0);
        store.store(WorkerResultStore.State.SUCCESS);
        store.store(WorkerResultStore.State.FAILURE);

        BufferedReader br = new BufferedReader(new FileReader(new File(folder, "work.json")));
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
    }
}