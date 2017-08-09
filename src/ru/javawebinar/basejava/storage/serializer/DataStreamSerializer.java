package ru.javawebinar.basejava.storage.serializer;

import ru.javawebinar.basejava.model.*;

import java.io.*;
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
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(String.valueOf(entry.getValue()));
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
                String sectionType=SectionType.valueOf(dis.readUTF()).getTitle();
                if (sectionType.equals(SectionType.PERSONAL.getTitle()) || sectionType.equals(SectionType.ACHIEVEMENT.getTitle()) || sectionType.equals(SectionType.QUALIFICATIONS.getTitle()))
                resume.addSection(SectionType.valueOf(dis.readUTF()),new TextSection(dis.readUTF()));
            }

            return resume;
        }
    }
}
