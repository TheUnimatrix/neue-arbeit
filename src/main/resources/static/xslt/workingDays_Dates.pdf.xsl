<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.1"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:fo="http://www.w3.org/1999/XSL/Format">
	
	<xsl:template match="/root">
		
		<fo:root>
			<fo:layout-master-set>
				<!-- A4-Seite im Hochformat -->
				<fo:simple-page-master master-name="A4" page-width="210mm" page-height="297mm"
					margin-top="1.5cm" margin-bottom="1.5cm" margin-left="2.5cm" margin-right="2cm">
					<fo:region-body/>
				</fo:simple-page-master>
			</fo:layout-master-set>
			
			<fo:page-sequence master-reference="A4">
				 <fo:flow flow-name="xsl-region-body">
				 
				 	<!-- Überschrift -->
				 	<fo:block font-family="Arial, sans-serif" font-size="14pt" font-weight="bold" margin-top="20pt">
						<fo:inline text-decoration="underline">
							Monatliche Arbeitstage (<xsl:value-of select="month[position() = 1]/durationStart"/>
							- <xsl:value-of select="month[last()]/durationEnd"/>)
						</fo:inline>
					</fo:block>
				 	
					<!-- Tabelle mit Arbeitstagen -->
					<fo:table font-family="Arial, sans-serif" width="100%" margin-top="5pt" table-layout="fixed">
						
						<fo:table-column column-width="65%"/>
						<fo:table-column column-width="35%"/>
						
						<!-- Tabellen-Header (Spaltenüberschriften) -->
						<fo:table-header>
							<fo:table-row text-align="center" font-size="14pt" font-weight="bold">
								<fo:table-cell border="solid black 1pt" padding="3pt">
									<fo:block>Zeitraum</fo:block>
								</fo:table-cell>
								<fo:table-cell border="solid black 1pt" padding="3pt">
									<fo:block>Anzahl der Arbeitstage</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-header>
						
						<!-- Tabellen-Footer (Gesamtanzahl des Zeitraums) -->
						<fo:table-footer>
							<fo:table-row>
								<fo:table-cell font-size="14pt" padding="3pt">
									<fo:block>
										Gesamte Anzahl der Arbeitstage
									</fo:block>
								</fo:table-cell>
								<fo:table-cell text-align="center" font-size="14pt" padding="3pt">
									<fo:block>
										<!-- Berechne Gesamtanzahl der Arbeitstage für den gesamten Zeitraum -->
										<xsl:value-of select="sum(month/workingDays)"/>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-footer>
						
						<!-- Tabellen-Körper (einzelne Zeiträume mit jeweiliger Anzahl) -->
						<fo:table-body>
						
							<!-- Iteriere über alle Monate des gesamten Zeitraums -->
							<xsl:for-each select="month">
								<fo:table-row>
									<fo:table-cell font-size="14pt" border="solid black 1pt" padding="3pt">
										<fo:block>
											<xsl:value-of select="durationStart"/> - <xsl:value-of select="durationEnd"/>
										</fo:block>
									</fo:table-cell>
									<fo:table-cell text-align="center" font-size="14pt" border="solid black 1pt" padding="3pt">
										<fo:block>
											<xsl:value-of select="workingDays"/>
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</xsl:for-each>
						</fo:table-body>
					</fo:table>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
</xsl:stylesheet>