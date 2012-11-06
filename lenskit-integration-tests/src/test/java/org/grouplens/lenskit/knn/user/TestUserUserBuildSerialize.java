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
package org.grouplens.lenskit.knn.user;

import org.grouplens.lenskit.ItemRecommender;
import org.grouplens.lenskit.ItemScorer;
import org.grouplens.lenskit.RecommenderBuildException;
import org.grouplens.lenskit.baseline.BaselinePredictor;
import org.grouplens.lenskit.baseline.ItemUserMeanPredictor;
import org.grouplens.lenskit.core.LenskitRecommender;
import org.grouplens.lenskit.core.LenskitRecommenderEngine;
import org.grouplens.lenskit.core.LenskitRecommenderEngineFactory;
import org.grouplens.lenskit.test.ML100KTestSuite;
import org.grouplens.lenskit.transform.normalize.BaselineSubtractingUserVectorNormalizer;
import org.grouplens.lenskit.transform.normalize.UserVectorNormalizer;
import org.grouplens.lenskit.vectors.similarity.CosineVectorSimilarity;
import org.grouplens.lenskit.vectors.similarity.VectorSimilarity;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Do major tests on the user-user recommender.
 *
 * @author Michael Ekstrand
 */
public class TestUserUserBuildSerialize extends ML100KTestSuite {
    @Test
    public void testBuildAndSerializeModel() throws RecommenderBuildException, IOException {
        LenskitRecommenderEngineFactory factory = new LenskitRecommenderEngineFactory(daoFactory);
        factory.bind(ItemRecommender.class)
               .to(UserUserRecommender.class);
        factory.bind(ItemScorer.class)
               .to(UserUserRatingPredictor.class);
        factory.within(UserVectorSimilarity.class)
               .bind(VectorSimilarity.class)
               .to(CosineVectorSimilarity.class);
        factory.bind(UserVectorNormalizer.class)
               .to(BaselineSubtractingUserVectorNormalizer.class);
        factory.bind(BaselinePredictor.class)
               .to(ItemUserMeanPredictor.class);

        LenskitRecommenderEngine engine = factory.create();
        assertThat(engine, notNullValue());

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        engine.write(out);
        byte[] bytes = out.toByteArray();

        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        LenskitRecommenderEngine loaded = LenskitRecommenderEngine.load(daoFactory, in);
        assertThat(loaded, notNullValue());

        LenskitRecommender rec = loaded.open();
        try {
            assertThat(rec.getRatingPredictor(),
                       instanceOf(UserUserRatingPredictor.class));
        } finally {
            rec.close();
        }
    }
}
