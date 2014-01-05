VkMusicDownloader
=================

Graphic downloader for music in Vk profile (currently only yours)


## Puproses

This software let's you easily download audio tracks from your Vk profile in format you prefer

## Executing

There's 2 ways to use this tool:

1.  Download the latest built version from [my build-server](http://jenkins.dginz.org/job/VkMusicDownloader/lastSuccessfulBuild/artifact/build/distributions/VkMusicDownloader.zip) and start bin/VkMusicDownloader(.bat)

    You must have [Oracle Java of version at least 7u15](http://java.com/en/download/manual.jsp) installed
2.  Checkout project with `git clone https://github.com/ginz/VkMusicDownloader.git`

    Install [gradle](http://gradle.org), [JDK of version at least 7u15](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html)

    Run `gradle run` to run tool, `gradle jar` to deploy it as jar or `gradle distZip` to deploy it as zip (like the version on build-server)


## Security

This software is open-source, so if you're not sure in your security, you can read the code and see, that nothing is backdoored or cracked
