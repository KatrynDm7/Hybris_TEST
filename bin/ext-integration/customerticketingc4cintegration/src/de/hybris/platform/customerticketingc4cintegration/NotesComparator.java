/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.platform.customerticketingc4cintegration;

import de.hybris.platform.customerticketingc4cintegration.data.Note;

import java.util.Comparator;

public class NotesComparator implements Comparator<Note>
{
    @Override
    public int compare(Note o1, Note o2)
    {
        return o1.getCreatedOn().compareTo(o2.getCreatedOn());
    }
}
