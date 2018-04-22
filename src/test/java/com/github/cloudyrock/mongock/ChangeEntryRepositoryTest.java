package com.github.cloudyrock.mongock;

import com.github.fakemongo.Fongo;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.Test;

import java.util.Date;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author lstolowski
 * @since 10.12.14
 */
public class ChangeEntryRepositoryTest {
  private static final String TEST_SERVER = "testServer";
  private static final String DB_NAME = "mongocktest";
  private static final String CHANGELOG_COLLECTION_NAME = "dbchangelog";

  @Test
  public void shouldCreateChangeIdAuthorIndexIfNotFound() throws MongockException {

    // given

    MongoDatabase db = new Fongo(TEST_SERVER).getDatabase(DB_NAME);

    // when
    ChangeEntryRepository dao = spy(new ChangeEntryRepository(CHANGELOG_COLLECTION_NAME, db));
    doReturn(null).when(dao).findRequiredChangeAndAuthorIndex();

    dao.isNewChange(new ChangeEntry("changeId", "author", new Date(), "class", "method"));

    //then
    verify(dao, times(1)).createRequiredUniqueIndex();
    // and not
    verify(dao, times(0)).dropIndex(any(Document.class));
  }

  @Test
  public void shouldNotCreateChangeIdAuthorIndexIfFound() throws MongockException {

    // given
    MongoClient mongoClient = mock(MongoClient.class);
    MongoDatabase db = new Fongo(TEST_SERVER).getDatabase(DB_NAME);
    when(mongoClient.getDatabase(anyString())).thenReturn(db);

    MongoRepository mongoRepositoryMock = mock(MongoRepository.class);
    when(mongoRepositoryMock.findRequiredChangeAndAuthorIndex()).thenReturn(new Document());
    when(mongoRepositoryMock.isUnique(any(Document.class))).thenReturn(true);

    // when
    new ChangeEntryRepository(CHANGELOG_COLLECTION_NAME, mongoClient.getDatabase(DB_NAME));

    //then
    verify(mongoRepositoryMock, times(0)).createRequiredUniqueIndex();
    // and not
    verify(mongoRepositoryMock, times(0)).dropIndex(new Document());
  }

  @Test
  public void shouldRecreateChangeIdAuthorIndexIfFoundNotUnique() throws MongockException {
    // given
    MongoDatabase db = new Fongo(TEST_SERVER).getDatabase(DB_NAME);
    ChangeEntryRepository dao = spy(new ChangeEntryRepository(CHANGELOG_COLLECTION_NAME, db));
    doReturn(new Document()).when(dao).findRequiredChangeAndAuthorIndex();
    doReturn(false).when(dao).isUnique(any(Document.class));
    doNothing().when(dao).dropIndex(any(Document.class));

    // when
    dao.isNewChange(new ChangeEntry("changeId", "author", new Date(), "class", "method"));

    //then
    verify(dao, times(1)).dropIndex(any(Document.class));
    // and
    verify(dao, times(1)).createRequiredUniqueIndex();
  }

}
