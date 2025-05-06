# MailManager

MailManager est une application Java de gestion de la messagerie visant à offrir des fonctionnalités généralement absentes des serveurs classiques (Gmail, Yahoo, Hotmail, etc.) telles que la classification automatique, les mailing lists, l'archivage XML, la recherche multicritères, la gestion de plusieurs boîtes et la communication mobile (SMS/Email).

## Fonctionnalités principales

- **Envoi et réception d'emails** (IMAP/SMTP)
- **Gestion des mailing lists** pour envois groupés et programmés
- **Classification automatique** des messages (spam, priorité)
- **Archivage au format XML** et **recherche multicritères**
- **Gestion multi-comptes** email
- **Notification mobile** (SMS et push)

## Prérequis

- Java 8 ou supérieur
- Bibliothèques externes (JAR) dans le dossier \`lib/\` : JavaMail, API SMS, etc.
- Serveur IMAP/SMTP accessible pour les services email

## Installation

1. Cloner le dépôt :
   \`\`\`bash
   git clone https://votre-repo/MailManager.git
   \`\`\`
2. Placer les JAR externes dans \`lib/\`.
3. Compiler :
   \`\`\`bash
   javac -cp "lib/*" -d out src/java/com/mailmanager/**/*.java
   \`\`\`

## Arborescence du projet

\`\`\`text
├── .gitignore
├── README.md                              ← Fichier de documentation
├── lib/                                   ← JAR externes (JavaMail, SMS API…)
└── src/
    └── java/
        └── com/mailmanager/
            ├── MailManagerApp.java        ← Point d’entrée de l’application
            │
            ├── email/                      ← Tâche 1 : Envoi & réception
            │   ├── EmailReceiverService.java  ← réception des emails
            │   ├── EmailSenderService.java    ← envoi des emails
            │   └── EmailClient.java           ← connexion IMAP/SMTP
            │
            ├── mailing/                    ← Tâche 2 : Listes de diffusion
            │   ├── MailingListService.java
            │   └── SubscriberService.java
            │
            ├── classification/            ← Tâche 3 : Classification automatique
            │   ├── SpamClassifier.java
            │   └── PriorityClassifier.java
            │
            ├── archive/                   ← Tâche 4 : Archivage & recherche
            │   ├── ArchiveService.java
            │   └── SearchService.java
            │
            ├── accounts/                  ← Tâche 5 : Multi-boîtes email
            │   └── AccountService.java
            │
            ├── notification/              ← Tâche 6 : Notification mobile
            │   ├── SMSService.java
            │   └── PushNotificationService.java
            │
            └── util/                      ← Outils partagés
                ├── ConfigLoader.java
                └── LogManager.java
\`\`\`

## Démarrage de l'application

\`\`\`bash
java -cp "lib/*:out" com.mailmanager.MailManagerApp
\`\`\`

## Contribution

Les contributions sont les bienvenues ! Veuillez ouvrir une issue ou soumettre une pull request.

## Licence

Ce projet est sous licence MIT.
EOF

