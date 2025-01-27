package io.mongock.runner.core.executor;

import io.mongock.runner.core.internal.ChangeLogItem;
import io.mongock.runner.core.internal.ChangeSetItem;
import io.mongock.api.config.MongockConfiguration;
import io.mongock.api.exception.MongockException;
import io.mongock.driver.api.driver.ConnectionDriver;
import io.mongock.runner.core.executor.changelog.ChangeLogRuntime;
import io.mongock.runner.core.executor.operation.Operation;
import io.mongock.runner.core.executor.operation.change.MigrationExecutor;
import io.mongock.runner.core.executor.operation.change.MigrationOp;

import java.util.SortedSet;

public class ExecutorFactoryDefault implements ExecutorFactory<ChangeLogItem<ChangeSetItem>, ChangeSetItem, MongockConfiguration> {

  @Override
  public  Executor getExecutor(Operation op,
								 String executionId,
								 SortedSet<ChangeLogItem<ChangeSetItem>> changeLogs,
								 ConnectionDriver driver,
								 ChangeLogRuntime changeLogRuntime,
								 MongockConfiguration config) {
    switch (op.getId()) {

      case MigrationOp.ID:
        return new MigrationExecutor(executionId, changeLogs, driver, changeLogRuntime, config);

      default:
        throw new MongockException(String.format("Operation '%s' not found. It may be a professional operation and the professional library is not provided ", op.getId()));
    }
  }


}


