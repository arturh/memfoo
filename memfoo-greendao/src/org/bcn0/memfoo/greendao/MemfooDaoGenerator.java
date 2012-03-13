/*
* Copyright (C) 2011 Markus Junginger, greenrobot (http://greenrobot.de)
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.bcn0.memfoo.greendao;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

/**
* Generates entities and DAOs for the example project DaoExample.
* 
* Run it as a Java application (not Android).
* 
* @author Markus
*/
public class MemfooDaoGenerator {

   public static void main(String[] args) throws Exception {
       Schema schema = new Schema(1, "org.bcn0.memfoo");

       addCard(schema);

       new DaoGenerator().generateAll(schema, "../Memfoo/src-gen");
   }

   private static void addCard(Schema schema) {
       Entity card = schema.addEntity("Card");
       card.addIdProperty();
       card.addStringProperty("kanji");
       card.addStringProperty("kana");
       card.addStringProperty("meaning");
       card.addStringProperty("audio");
       card.addDateProperty("due");
       card.addDateProperty("introduced");
       card.addIntProperty("correct");
   }

}

