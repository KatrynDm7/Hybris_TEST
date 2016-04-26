package de.hybris.platform.warehousing.comment.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.comments.model.CommentTypeModel;
import de.hybris.platform.comments.model.ComponentModel;
import de.hybris.platform.comments.model.DomainModel;
import de.hybris.platform.comments.services.CommentDao;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.warehousing.comment.WarehousingCommentService;
import de.hybris.platform.warehousing.data.comment.WarehousingCommentContext;
import de.hybris.platform.warehousing.data.comment.WarehousingCommentEventType;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class AbstractWarehousingCommentServiceTest
{
	@InjectMocks
	private final WarehousingCommentService<ConsignmentEntryModel> service = new DefaultConsignmentEntryCommentService();
	@Mock
	private ModelService modelService;
	@Mock
	private TimeService timeService;
	@Mock
	private UserService userService;
	@Mock
	private CommentDao commentDao;
	@Mock
	private FlexibleSearchService flexibleSearchService;
	@Mock
	private UserModel author;

	private CommentTypeModel commentType;
	private ComponentModel component;
	private CommentModel comment;
	private ItemModel item;
	private WarehousingCommentContext context;
	private DomainModel domain;

	@Before
	public void setUp()
	{
		item = new ItemModel();
		comment = new CommentModel();
		context = new WarehousingCommentContext();

		domain = new DomainModel();
		domain.setCode("Domain code");
		domain.setName("Domain name");

		component = new ComponentModel();
		component.setCode("Component code");
		component.setName("Component name");

		commentType = new CommentTypeModel();
		commentType.setCode(WarehousingCommentEventType.CANCEL_CONSIGNMENT_COMMENT.getCommentTypeCode());
		commentType.setName(WarehousingCommentEventType.CANCEL_CONSIGNMENT_COMMENT.getCommentTypeName());

		item.setComments(Collections.emptyList());

		context.setCommentType(WarehousingCommentEventType.CANCEL_ORDER_COMMENT);
		context.setItem(item);
		context.setSubject("Subject");
		context.setText("Text body");

		when(modelService.create(CommentModel.class)).thenReturn(comment);
		when(modelService.create(CommentTypeModel.class)).thenReturn(commentType);
		when(modelService.create(ComponentModel.class)).thenReturn(component);
		when(timeService.getCurrentTime()).thenReturn(new Date(System.currentTimeMillis()));
		when(userService.getCurrentUser()).thenReturn(new UserModel());
		when(commentDao.findDomainsByCode("warehousing")).thenReturn(Arrays.asList(domain));
	}

	@Test
	public void shouldCreate_SingleComment()
	{
		service.createAndSaveComment(context);

		verify(modelService).create(CommentModel.class);
		verify(modelService).saveAll();

		assertEquals(item, comment.getOwner());
		assertEquals("Subject", comment.getSubject());
		assertEquals("Text body", comment.getText());
		assertTrue(comment.getCreationtime().compareTo(new Date(System.currentTimeMillis())) < 1);
		assertNotNull(comment.getAuthor());
		assertNotNull(comment.getCommentType());
		assertNotNull(comment.getComponent());
	}

	@Test
	public void shouldCreate_MultipleComments()
	{
		service.createAndSaveComment(context);
		service.createAndSaveComment(context);

		verify(modelService, times(2)).create(CommentModel.class);
		verify(modelService, times(2)).saveAll();

		assertNotNull(item.getComments());
		assertFalse(item.getComments().isEmpty());
		assertEquals(2, item.getComments().size());
	}

	@Test
	public void shouldCreate_NullSubject()
	{
		context.setSubject(null);
		service.createAndSaveComment(context);

		verify(modelService).create(CommentModel.class);
		verify(modelService).saveAll();

		assertEquals(item, comment.getOwner());
		assertNull(comment.getSubject());
		assertEquals("Text body", comment.getText());
		assertTrue(comment.getCreationtime().compareTo(new Date(System.currentTimeMillis())) < 1);
		assertNotNull(comment.getAuthor());
		assertNotNull(comment.getCommentType());
		assertNotNull(comment.getComponent());
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFail_NullText()
	{
		context.setText(null);
		service.createAndSaveComment(context);

		verifyZeroInteractions(modelService);
		verifyZeroInteractions(timeService);
		assertNull(item.getComments());
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFail_NullItem()
	{
		context.setItem(null);
		service.createAndSaveComment(context);

		verifyZeroInteractions(modelService);
		verifyZeroInteractions(timeService);
		assertNull(item.getComments());
	}

	@Test
	public void shouldCreateDomain()
	{
		final DomainModel newDomain = new DomainModel();
		when(commentDao.findDomainsByCode("warehousing")).thenReturn(Collections.emptyList());
		when(modelService.create(DomainModel.class)).thenReturn(newDomain);

		final DomainModel domain = service.getOrCreateDomainForCodeWarehousing();

		verify(modelService).save(newDomain);
		assertNotNull(domain);
		assertEquals("warehousing", domain.getCode());
		assertEquals("Warehousing Domain", domain.getName());
	}

	@Test
	public void shouldGetDomain()
	{
		final DomainModel domain = service.getOrCreateDomainForCodeWarehousing();

		verify(modelService, never()).save(any());
		assertNotNull(domain);
		assertEquals("Domain code", domain.getCode());
		assertEquals("Domain name", domain.getName());
	}

	@Test
	public void shouldCreateComponent()
	{
		final ComponentModel newComponent = new ComponentModel();
		when(
				commentDao.findComponentsByDomainAndCode(domain,
						WarehousingCommentEventType.CANCEL_CONSIGNMENT_COMMENT.getComponentCode())).thenReturn(Collections.emptyList());
		when(modelService.create(ComponentModel.class)).thenReturn(newComponent);

		final ComponentModel component = service.getOrCreateComponent(WarehousingCommentEventType.CANCEL_CONSIGNMENT_COMMENT);

		verify(modelService).save(newComponent);
		assertNotNull(component);
		assertEquals(WarehousingCommentEventType.CANCEL_CONSIGNMENT_COMMENT.getComponentCode(), component.getCode());
		assertEquals(WarehousingCommentEventType.CANCEL_CONSIGNMENT_COMMENT.getComponentName(), component.getName());
	}

	@Test
	public void shouldGetComponent()
	{
		when(
				commentDao.findComponentsByDomainAndCode(domain,
						WarehousingCommentEventType.CANCEL_CONSIGNMENT_COMMENT.getComponentCode()))
		.thenReturn(Arrays.asList(component));

		final ComponentModel newComponent = service.getOrCreateComponent(WarehousingCommentEventType.CANCEL_CONSIGNMENT_COMMENT);

		verify(modelService, never()).save(any());
		assertNotNull(newComponent);
		assertEquals(component.getCode(), newComponent.getCode());
		assertEquals(component.getName(), newComponent.getName());
	}

	@Test
	public void shouldCreateCommentType()
	{
		when(modelService.create(ComponentModel.class)).thenReturn(commentType);
		component.setDomain(null);
		when(
				commentDao.findComponentsByDomainAndCode(domain,
						WarehousingCommentEventType.CANCEL_CONSIGNMENT_COMMENT.getComponentCode()))
		.thenReturn(Arrays.asList(component));

		final CommentTypeModel newCommentType = service
				.getOrCreateCommentType(WarehousingCommentEventType.CANCEL_CONSIGNMENT_COMMENT);

		verify(modelService).save(commentType);
		assertNotNull(newCommentType);
		assertEquals(WarehousingCommentEventType.CANCEL_CONSIGNMENT_COMMENT.getCommentTypeCode(), newCommentType.getCode());
		assertEquals(WarehousingCommentEventType.CANCEL_CONSIGNMENT_COMMENT.getCommentTypeName(), newCommentType.getName());
	}

	@Test
	public void shouldGetCommentType()
	{
		component.setDomain(domain);
		domain.setCommentTypes(Arrays.asList(commentType));
		when(
				commentDao.findComponentsByDomainAndCode(domain,
						WarehousingCommentEventType.CANCEL_CONSIGNMENT_COMMENT.getComponentCode()))
		.thenReturn(Arrays.asList(component));

		final CommentTypeModel newCommentType = service
				.getOrCreateCommentType(WarehousingCommentEventType.CANCEL_CONSIGNMENT_COMMENT);

		verify(modelService, never()).save(any());
		assertNotNull(newCommentType);
		assertEquals(commentType.getCode(), newCommentType.getCode());
		assertEquals(commentType.getName(), newCommentType.getName());
	}
}
