package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.Mockito;

import gnn.com.googlealbumdownloadappnougat.MainActivity;
import gnn.com.googlealbumdownloadappnougat.auth.PermissionHandler;
import gnn.com.googlealbumdownloadappnougat.auth.Require;

public class PermissionHandlerTest {

    @Test
    public void startRequirement () {
        // TODO
        // given
        MainActivity view = Mockito.mock(MainActivity.class);
        PermissionHandler permissionHandler = new PermissionHandler();
        Require require = Mockito.mock(Require.class);

        // when
        permissionHandler.startRequirement(require);

        // then
        // verify that the require is stored and started
        assertEquals(require, permissionHandler.getPendingRequirement());
        verify(require).exec();
    }
}