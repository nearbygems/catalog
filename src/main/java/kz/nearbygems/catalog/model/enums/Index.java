package kz.nearbygems.catalog.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Index {
  TAGS("tags"),
  STORAGE("storage");

  private final String code;
}
