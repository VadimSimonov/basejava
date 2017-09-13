CREATE TABLE resume (
  uuid      CHAR(36) PRIMARY KEY NOT NULL,
  full_name TEXT                 NOT NULL
);

CREATE TABLE contact (
  id          SERIAL,
  resume_uuid CHAR(36) NOT NULL REFERENCES resume (uuid) ON DELETE CASCADE,
  type        TEXT     NOT NULL,
  value       TEXT     NOT NULL
);
CREATE UNIQUE INDEX contact_uuid_type_index
  ON contact (resume_uuid, type);

CREATE TABLE section
(
  id          SERIAL   NOT NULL
    CONSTRAINT section_pkey
    PRIMARY KEY,
  sectiontype        TEXT     NOT NULL,
  name       TEXT     NOT NULL,
  resume_uuid CHAR(36) NOT NULL
    CONSTRAINT section_resume_uuid_fk
    REFERENCES resume
    ON UPDATE RESTRICT ON DELETE CASCADE
);
CREATE UNIQUE INDEX section_uuid_type_index
  ON section (resume_uuid, sectiontype );
CREATE INDEX fki_section_resume_uuid_fk
  ON section (resume_uuid);