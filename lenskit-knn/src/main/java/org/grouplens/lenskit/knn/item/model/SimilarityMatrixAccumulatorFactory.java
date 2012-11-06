/*
 * LensKit, an open source recommender systems toolkit.
 * Copyright 2010-2012 Regents of the University of Minnesota and contributors
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package org.grouplens.lenskit.knn.item.model;

import it.unimi.dsi.fastutil.longs.LongSortedSet;
import org.grouplens.grapht.annotation.DefaultImplementation;

/**
 * Interface of factories providing which provide a {@link SimilarityMatrixAccumulator}.
 * This interface allows for choosing between the {@link SimilarityMatrixAccumulator} options.
 */
@DefaultImplementation(SimpleSimilarityMatrixAccumulatorFactory.class)
public interface SimilarityMatrixAccumulatorFactory {

    /**
     * Creates and returns a SimilarityMatrixModel.
     *
     * @param itemUniverse The universe of items the accumulator will accumulate.
     * @return A SimilarityMatrixAccumulator.
     */
    public SimilarityMatrixAccumulator create(LongSortedSet itemUniverse);

}
