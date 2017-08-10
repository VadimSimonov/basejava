package ru.javawebinar.basejava.storage.serializer;

import ru.javawebinar.basejava.model.*;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
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


            Map<SectionType,Section>section = r.getSections();
            dos.writeInt(section.size());
            for (Map.Entry<SectionType,Section> entry:section.entrySet())
            {
                SectionType sectionType=entry.getKey();
                if (sectionType==SectionType.PERSONAL || sectionType==SectionType.OBJECTIVE) {
                    dos.writeUTF(new TextSection(entry.getKey().name()).getContent());
                    dos.writeUTF(new TextSection(entry.getValue().toString()).getContent());
                } else if (sectionType==SectionType.ACHIEVEMENT || sectionType==SectionType.QUALIFICATIONS) {
                    List<String>list=new ListSection(entry.getKey().name()).getItems();
                    List<String>list2=new ListSection(entry.getValue().toString()).getItems();
                    dos.writeUTF(list.iterator().next());
                    dos.writeUTF(list2.iterator().next());
                } else if (sectionType==SectionType.EXPERIENCE || sectionType==SectionType.EDUCATION) {
                    new Organization.Position().getStartDate();
                    dos.writeUTF(new OrganizationSection(
                    new Organization(new Link(entry.getKey().name(),entry.getValue().toString()),
                            new Organization.Position(DataFormatMethod(entry.getValue().toString()).,))));
                }
            }
        }
    }

    private LocalDate DataFormatMethod(String name) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("mm/yyyy");
        formatter = formatter.withLocale( Locale.ENGLISH);
        return LocalDate.parse(name, formatter);
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
                   // resume.addSection(SectionType.valueOf(dis.readUTF()), new OrganizationSection(new Organization(dis.readUTF(),"2",)));
                }
            }

            return resume;
        }
    }
}
