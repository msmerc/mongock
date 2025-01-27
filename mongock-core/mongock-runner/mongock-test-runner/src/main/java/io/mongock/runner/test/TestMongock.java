package io.mongock.runner.test;

import io.mongock.runner.core.internal.ChangeLogItem;
import io.mongock.runner.core.internal.ChangeSetItem;
import io.mongock.api.config.MongockConfiguration;
import io.mongock.runner.core.builder.RunnerBuilder;
import io.mongock.runner.core.builder.RunnerBuilderBase;
import io.mongock.runner.core.executor.ExecutorFactory;
import io.mongock.runner.core.executor.ExecutorFactoryDefault;
import io.mongock.runner.core.executor.changelog.ChangeLogService;
import io.mongock.runner.core.executor.dependency.DependencyManager;

import static io.mongock.runner.core.builder.BuilderType.COMMUNITY;

public class TestMongock {

  public static Builder builder() {
    return new Builder(new ExecutorFactoryDefault());
  }

  public static class Builder extends RunnerBuilderBase<Builder, ChangeLogItem<ChangeSetItem>, ChangeSetItem, MongockConfiguration> implements RunnerBuilder<Builder, MongockConfiguration> {

    private Builder(ExecutorFactory<ChangeLogItem<ChangeSetItem>, ChangeSetItem, MongockConfiguration> executorFactory) {
      super(COMMUNITY, executorFactory, new ChangeLogService(), new DependencyManager(), new MongockConfiguration());
    }

//    @Override
//    protected void beforeBuildRunner() {
//
//    }

    @Override
    public Builder getInstance() {
      return this;
    }

  }
}
