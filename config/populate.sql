INSERT INTO resume (uuid, full_name) VALUES
  ('7de882da-02f2-4d16-8daa-60660aaf4071', 'Name1'),
  ('a97b3ac3-3817-4c3f-8a5f-178497311f1d', 'Name2'),
  ('dd0a70d1-5ed3-479a-b452-d5e04f21ca73', 'Name3');
INSERT INTO contact (type,"value",resume_uuid) VALUES
  ('SKYPE','skype1', '7de882da-02f2-4d16-8daa-60660aaf4071'),
  ('SKYPE','skype2', 'a97b3ac3-3817-4c3f-8a5f-178497311f1d'),
  ('SKYPE','skype3', 'dd0a70d1-5ed3-479a-b452-d5e04f21ca73');