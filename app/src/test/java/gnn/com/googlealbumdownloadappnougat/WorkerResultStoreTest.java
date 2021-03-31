package gnn.com.googlealbumdownloadappnougat;

import org.junit.Test;

import static org.junit.Assert.*;

import androidx.work.Data;
import androidx.work.WorkInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class WorkerResultStoreTest {

    WorkerResultStore store = new WorkerResultStore();

    @Test
    public void store_with_empty() throws IOException {
        WorkInfo info = new WorkInfo(
                new UUID(123, 456),
                WorkInfo.State.ENQUEUED,
                new Data.Builder().build(),
                new ArrayList<String>(),
                new Data.Builder().build(),
                0);
        store.store(info);
    }
}