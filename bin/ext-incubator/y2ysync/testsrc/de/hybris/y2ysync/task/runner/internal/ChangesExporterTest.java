package de.hybris.y2ysync.task.runner.internal;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.deltadetection.ItemChangeDTO;
import de.hybris.deltadetection.enums.ChangeType;
import de.hybris.platform.core.model.type.ComposedTypeModel;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import com.google.common.collect.ImmutableList;


@UnitTest
public class ChangesExporterTest
{
	private static final String TYPE_CODE = ComposedTypeModel._TYPECODE;
	private static final String IMPEX_HEADER = "code[unique=true];name[lang=de]";

	@Test
	public void shouldGenerateEmptyOutputWhenThereIsNoChanges()
	{
		final List<ItemChangeDTO> changes = ImmutableList.of();
		final ExportScriptCreator creator = new ExportScriptCreator(IMPEX_HEADER, TYPE_CODE, changes);

		final String script = creator.createInsertUpdateExportScript();

		assertThat(script).isEmpty();
	}

	@Test
	public void shouldGenerateOnlyInsertUpdateScriptForNewItem()
	{
		final List<ItemChangeDTO> changes = ImmutableList.of(added(1));
		final ExportScriptCreator creator = new ExportScriptCreator(IMPEX_HEADER, TYPE_CODE, changes);

		final String script = creator.createInsertUpdateExportScript();

		assertThat(insertedOrUpdatedPartOF(script)).containsOnly(Long.valueOf(1));
		assertThat(removedPartOf(script)).isEmpty();
	}

	@Test
	public void shouldGenerateOnlyInsertUpdateScriptForModifiedItem()
	{
		final List<ItemChangeDTO> changes = ImmutableList.of(modified(2));
		final ExportScriptCreator creator = new ExportScriptCreator(IMPEX_HEADER, TYPE_CODE, changes);

		final String script = creator.createInsertUpdateExportScript();

		assertThat(insertedOrUpdatedPartOF(script)).containsOnly(Long.valueOf(2));
		assertThat(removedPartOf(script)).isEmpty();
	}

	@Test
	public void shouldGenerateOnlyInsertUpdateScriptForModifiedAndAddedItems()
	{
		final List<ItemChangeDTO> changes = ImmutableList.of(modified(4), added(5));
		final ExportScriptCreator creator = new ExportScriptCreator(IMPEX_HEADER, TYPE_CODE, changes);

		final String script = creator.createInsertUpdateExportScript();

		assertThat(insertedOrUpdatedPartOF(script)).containsOnly(Long.valueOf(5), Long.valueOf(4));
		assertThat(removedPartOf(script)).isEmpty();
	}

	private static final List<Long> removedPartOf(final String script)
	{
		final String removedPart = StringUtils.substringBetween(script, "REMOVE", "})\"\n");

		return extractPKs(removedPart);
	}

	private static final List<Long> insertedOrUpdatedPartOF(final String script)
	{
		final String insertUpdatePart = StringUtils.substringBetween(script, "INSERT_UPDATE", "})\"\n");

		return extractPKs(insertUpdatePart);
	}

	private static List<Long> extractPKs(final String impex)
	{
		if (impex == null)
		{
			return ImmutableList.of();
		}

		final ImmutableList.Builder resultBuilder = ImmutableList.builder();

		final Pattern pattern = Pattern.compile("\"\"(\\d+)\"\"");
		final Matcher matcher = pattern.matcher(impex);

		while (matcher.find())
		{
			resultBuilder.add(Long.valueOf(matcher.group(1)));
		}

		return resultBuilder.build();
	}

	private static ItemChangeDTO added(final long pk)
	{
		return new ItemChangeDTO(Long.valueOf(pk), new Date(), ChangeType.NEW, "Added", TYPE_CODE, "testStream");
	}

	private static ItemChangeDTO modified(final long pk)
	{
		return new ItemChangeDTO(Long.valueOf(pk), new Date(), ChangeType.MODIFIED, "Modified", TYPE_CODE, "testStream");
	}

	private static ItemChangeDTO removed(final long pk)
	{
		return new ItemChangeDTO(Long.valueOf(pk), new Date(), ChangeType.DELETED, String.valueOf(pk), TYPE_CODE, "testStream");
	}
}
