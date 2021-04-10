package gnn.com.googlealbumdownloadappnougat.service;

import android.content.Context;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import gnn.com.googlealbumdownloadappnougat.service.WorkerResultStore;

@RunWith(PowerMockRunner.class)
public class WorkerResultStoreTest {

    @Rule
    public TemporaryFolder tmpFolder = new TemporaryFolder();

    @Test
    public void store_with_empty() throws IOException {
        Context mock = mock(Context.class);
        when(mock.getCacheDir()).thenReturn(tmpFolder.newFile());
        WorkerResultStore store = new WorkerResultStore(mock);
//        WorkInfo info = new WorkInfo(
//                new UUID(123, 456),
//                WorkInfo.State.ENQUEUED,
//                new Data.Builder().build(),
//                new ArrayList<String>(),
//                new Data.Builder().build(),
//                0);
        store.store(WorkerResultStore.State.SUCCESS);
        store.store(WorkerResultStore.State.FAILURE);
    }
}