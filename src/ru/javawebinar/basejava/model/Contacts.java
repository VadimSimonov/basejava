package ru.javawebinar.basejava.model;

/**
 * Created by simonov on 7/10/17.
 */
    public enum Contacts {
        ADDRESS("Адрес"),
        MAIL("Почта"),
        SKYPE("Skype"),
        MOBILE_PHONE("Телефон для связи"),
        PROFILE_HABRRAHABR("Профиль Habrahabr"),
        PROFILE_STACKOVERFLOW("Профиль StackOverflow"),
        PROFILE_GITHUB("аккаунт GitHub");

        private String title;

        Contacts(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
}
