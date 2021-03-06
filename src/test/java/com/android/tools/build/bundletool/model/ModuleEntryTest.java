/*
 * Copyright (C) 2017 The Android Open Source Project
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
 * limitations under the License
 */

package com.android.tools.build.bundletool.model;

import static com.android.tools.build.bundletool.testing.TestUtils.toByteArray;
import static com.google.common.truth.Truth.assertThat;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.function.Supplier;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ModuleEntryTest {
  @Test
  public void builder() throws Exception {
    ZipPath path = ZipPath.create("a");
    byte[] content = new byte[] {'a'};
    ModuleEntry entry = createEntry(path, content).toBuilder().setShouldCompress(false).build();

    assertThat(entry.getPath()).isEqualTo(path);
    assertThat(entry.getShouldCompress()).isFalse();
    assertThat(toByteArray(entry.getContentSupplier())).isEqualTo(content);
  }

  @Test
  public void builder_defaults() throws Exception {
    ModuleEntry entry = createEntry(ZipPath.create("a"), new byte[0]);
    assertThat(entry.getShouldCompress()).isTrue();
  }

  @Test
  public void equals_differentPath() throws Exception {
    ModuleEntry entry1 = createEntry(ZipPath.create("a"), new byte[0]);
    ModuleEntry entry2 = createEntry(ZipPath.create("b"), new byte[0]);

    assertThat(entry1.equals(entry2)).isFalse();
  }

  @Test
  public void equals_differentFileContents() throws Exception {
    ModuleEntry entry1 = createEntry(ZipPath.create("a"), new byte[] {'a'});
    ModuleEntry entry2 = createEntry(ZipPath.create("a"), new byte[] {'b'});

    assertThat(entry1.equals(entry2)).isFalse();
  }

  @Test
  public void equals_sameFiles() throws Exception {
    ModuleEntry entry = createEntry(ZipPath.create("a"), new byte[] {'a'});

    assertThat(entry.equals(entry)).isTrue();
  }

  private static ModuleEntry createEntry(ZipPath path, byte[] content) throws Exception {
    return createEntry(path, () -> new ByteArrayInputStream(content));
  }

  private static ModuleEntry createEntry(ZipPath path, Supplier<InputStream> contentSupplier) {
    return ModuleEntry.builder().setPath(path).setContentSupplier(contentSupplier::get).build();
  }
}
