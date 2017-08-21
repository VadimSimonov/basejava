package ru.javawebinar.basejava.storage.serializer;

import ru.javawebinar.basejava.model.*;

import java.io.*;
import java.text.DateFormatSymbols;
import java.time.Month;
import java.util.ArrayList;
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
                    dos.writeUTF(sectionType.name());
                    dos.writeUTF(((TextSection)section).getContent());
                }

                else if (sectionType==SectionType.ACHIEVEMENT || sectionType==SectionType.QUALIFICATIONS) {
                    List<String> list = (((ListSection) section).getItems());
                    dos.writeUTF(sectionType.name());
                    dos.writeInt(list.size());
                    for (String aList : list) {
                        dos.writeUTF(aList);
                    }
                } else if (sectionType==SectionType.EXPERIENCE || sectionType==SectionType.EDUCATION) {
                    List<Organization> list = (((OrganizationSection) section).getOrganizations());
                    dos.writeUTF(sectionType.name());
                    dos.writeInt(list.size());
                    for (Organization organization : list) {
                        dos.writeUTF(organization.getHomePage().getName());
                        String str = organization.getHomePage().getUrl();
                        if (str==null){
                            dos.writeUTF("");
                        }else
                            dos.writeUTF(str);

                        List<Organization.Position> positionList = organization.getPositions();
                        dos.writeInt(positionList.size());
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

            int sizeSection = dis.readInt();
            int sizeListA;
            int sizeListQ;
            int sizeOrg;
            //TODO implements sections
            for (int i = 0; i < sizeSection; i++) {
                SectionType sectionType = SectionType.valueOf(dis.readUTF());

                if (sectionType.equals(SectionType.PERSONAL)) {
                    resume.addSection(sectionType, new TextSection(dis.readUTF()));
                } else if (sectionType.equals(SectionType.OBJECTIVE))
                {
                    resume.addSection(sectionType, new TextSection(dis.readUTF()));
                }else if (sectionType.equals(SectionType.ACHIEVEMENT)) {
                    sizeListA=dis.readInt();
                    List<String> list = ReadList(dis,sizeListA);
                    resume.addSection(sectionType, new ListSection(list));

                } else if (sectionType.equals(SectionType.QUALIFICATIONS))
                    {
                        sizeListQ=dis.readInt();
                        List<String> list = ReadList(dis,sizeListQ);
                        resume.addSection(sectionType, new ListSection(list));

                    }else
                    if (sectionType.equals(SectionType.EXPERIENCE) || sectionType.equals(SectionType.EDUCATION)) {
                        sizeOrg = dis.readInt();
                            resume.addSection(sectionType, new OrganizationSection(WhileOrganization(sizeOrg,dis)));
                }
            }
            return resume;
        }
    }

    private List<Organization> WhileOrganization(int sizeOrg, DataInputStream dis) throws IOException {
        List<Organization> list = new ArrayList<>();
        for (int j = 0; j <sizeOrg ; j++) {
            String name = dis.readUTF();
            String url = dis.readUTF();
            int sizeP = dis.readInt();
            list.add(new Organization(new Link(name, url), WhilePosition(sizeP, dis)));
        }
        return list;
    }

    private List<Organization.Position> WhilePosition(int sizeP, DataInputStream dis) throws IOException {
        List<Organization.Position>list=new ArrayList<>();
        for (int i = 0; i <sizeP ; i++) {
            list.add(new Organization.Position(dis.readInt(),getMonth(dis.readInt()), dis.readInt(), getMonth(dis.readInt()), dis.readUTF(), dis.readUTF()));
        }

        return list;
    }

    private Month getMonth(int startM) {
        return Month.of(Month.valueOf(new DateFormatSymbols().getMonths()[startM-1].toUpperCase()).getValue());
    }

    private List<String> ReadList(DataInputStream dis, int sizeList) throws IOException {
        List<String> list=new ArrayList<>();
        for (int j = 0; j <sizeList ; j++) {
            list.add(dis.readUTF());
        }
        return list;
    }
}
