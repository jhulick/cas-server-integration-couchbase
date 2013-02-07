package se.kth.infosys.login.couchbase;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;

import org.jasig.cas.services.RegexRegisteredService;
import org.jasig.cas.services.RegisteredService;
import org.jasig.cas.services.RegisteredServiceImpl;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.couchbase.client.CouchbaseClient;

/**
 * Tests for the saving and finding ServiceRegistry classes.
 */
public class CouchbaseServiceRegistryTests {
	/* The subject for testing */
	private static CouchbaseServiceRegistryDaoImpl serviceRegistry = new CouchbaseServiceRegistryDaoImpl();

	/* Mock-ups for the database */
	private static CouchbaseClient client = mock(CouchbaseClient.class);
	private static CouchbaseClientFactory couchbase = mock(CouchbaseClientFactory.class);

	/* Hash map to store JSON services in for mocking */
	private static HashMap<String, String> services = new HashMap<String, String>();
	

	/*
	 * Set up mock up behavior for the database.
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		serviceRegistry.setCouchbase(couchbase);

		when(couchbase.getClient()).thenReturn(client);
		
		when(client.incr(eq("LAST_ID"), anyInt(), anyInt())).thenAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				return (long) services.size();
			}});

		when(client.get(anyString())).thenAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				return services.get((String) args[0]);
			}});

		when(client.set(anyString(), eq(0), anyString())).thenAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				services.put((String) args[0], (String) args[2]);
		        return null;
			}});
	}

	
	/*
	 * Store a RegisteredServiceImpl and see if we get the same back.
	 */
	@Test
	public void testSaveServiceImpl() {
		RegisteredServiceImpl registeredService = new RegisteredServiceImpl();

		ServiceJsonSerializerTests.setProperties(registeredService);
		assertEquals(-1, registeredService.getId());

		RegisteredService newService = serviceRegistry.save(registeredService);
		assertEquals(0, newService.getId());

		RegisteredService service = serviceRegistry.findServiceById(newService.getId());
		assertNotNull(service);
		assertTrue(service instanceof RegisteredServiceImpl);
		ServiceJsonSerializerTests.assertPropertiesEqual(newService, service);
	}

	
	/*
	 * Store a RegexRegisteredService and see if we get the same back.
	 */
	@Test
	public void testSaveRegexService() {
		RegexRegisteredService registeredService = new RegexRegisteredService();

		ServiceJsonSerializerTests.setProperties(registeredService);
		assertEquals(-1, registeredService.getId());

		RegisteredService newService = serviceRegistry.save(registeredService);
		assertEquals(1, newService.getId());

		RegisteredService service = serviceRegistry.findServiceById(newService.getId());
		assertNotNull(service);
		assertTrue(service instanceof RegexRegisteredService);
		ServiceJsonSerializerTests.assertPropertiesEqual(newService, service);
	}
}
