package ru.javawebinar.basejava.storage.serializer;

import ru.javawebinar.basejava.model.*;

import java.io.*;
import java.util.List;
import java.util.Map;

public class DataStreamSerializer implements StreamSerializer {

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());
            // TODO implements contacts
            Map<ContactType, String> contacts = r.getContacts();
            dos.writeInt(contacts.size());
            for (Map.Entry<ContactType, String> entry : contacts.entrySet()) {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            }
            // TODO implements sections
            Map<SectionType,Section>sectional = r.getSections();
            dos.writeInt(sectional.size());
            for (Map.Entry<SectionType,Section> entry:sectional.entrySet())
            {
                SectionType sectionType=entry.getKey();
                Section section=entry.getValue();
                if (sectionType==SectionType.PERSONAL || sectionType==SectionType.OBJECTIVE) {
                    dos.writeUTF(entry.getKey().name());
                    dos.writeUTF(((TextSection)section).getContent());
                }
                else if (sectionType==SectionType.ACHIEVEMENT || sectionType==SectionType.QUALIFICATIONS) {
                    int size=(((ListSection)section).getItems()).size();
                    List<String> list = (((ListSection) section).getItems());
                    dos.writeUTF(entry.getKey().name());
                    for (int i = 0; i <size ; i++) {
                        dos.writeUTF(list.get(i));
                    }
                } else if (sectionType==SectionType.EXPERIENCE || sectionType==SectionType.EDUCATION) {
                    List<Organization> list = (((OrganizationSection) section).getOrganizations());
                    dos.writeUTF(entry.getKey().name());
                    for (Organization organization : list) {
                        dos.writeUTF(organization.getHomePage().getName());
                        String str = organization.getHomePage().getUrl();
                        if (str==null){
                            dos.writeUTF("");
                        }else
                            dos.writeUTF(str);

                        List<Organization.Position> positionList = organization.getPositions();
                        for (Organization.Position aPositionList : positionList) {
                            dos.writeInt(aPositionList.getStartDate().getYear());
                            dos.writeInt(aPositionList.getStartDate().getMonth().getValue());
                            dos.writeInt(aPositionList.getEndDate().getYear());
                            dos.writeInt(aPositionList.getEndDate().getMonth().getValue());
                            dos.writeUTF(aPositionList.getTitle());
                            String str1=aPositionList.getDescription();
                            if (str1==null){
                                dos.writeUTF("");
                            }else
                            dos.writeUTF(aPositionList.getDescription());
                        }
                    }
                }
            }
        }
    }


    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            // TODO implements contacts
            Resume resume = new Resume(uuid, fullName);
            int size = dis.readInt();
            for (int i = 0; i < size; i++) {
                resume.addContact(ContactType.valueOf(dis.readUTF()), dis.readUTF());
            }
            // TODO implements sections
            int section = dis.readInt();
            for (int i = 0; i < section; i++) {

                String sectionType = String.valueOf(SectionType.valueOf(dis.readUTF()));
                SectionType st = SectionType.valueOf(dis.readUTF());

                System.out.println(SectionType.PERSONAL.name().equals(st));
                if (st.equals(SectionType.PERSONAL.getTitle()) || st.equals(SectionType.OBJECTIVE.getTitle())) {
                    resume.addSection(SectionType.valueOf(dis.readUTF()), new TextSection(dis.readUTF()));
                } else if (st.equals(SectionType.ACHIEVEMENT.getTitle()) || st.equals(SectionType.QUALIFICATIONS.getTitle())) {
                    resume.addSection(SectionType.valueOf(dis.readUTF()), new ListSection(dis.readUTF()));
                } else if (st.equals(SectionType.EXPERIENCE.getTitle()) || st.equals(SectionType.EDUCATION.getTitle())) {

                }
            }

            return resume;
        }
    }
}
