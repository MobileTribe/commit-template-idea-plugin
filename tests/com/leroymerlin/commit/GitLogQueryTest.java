package com.leroymerlin.commit;

import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

public class GitLogQueryTest {

    @Test
    @Ignore("manual testing")
    public void testExecute() {
        GitLogQuery.Result result = new GitLogQuery(new File("<absolute path>")).execute();

        System.out.println(result.isSuccess());
        System.out.println(result.getScopes());
    }

}