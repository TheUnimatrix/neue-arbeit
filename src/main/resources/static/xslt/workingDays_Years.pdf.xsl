<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:fo="http://www.w3.org/1999/XSL/Format">
	
	<xsl:template match="/">
		
		<fo:root>
			<fo:layout-master-set>
				<!-- A4-Seite im Hochformat -->
				<fo:simple-page-master master-name="A4"
					page-width="210mm" page-height="297mm"
					margin-top="1.5cm" margin-bottom="1.5cm"
					margin-left="2.5cm" margin-right="2cm">
					<fo:region-body/>
				</fo:simple-page-master>
			</fo:layout-master-set>
			
			<fo:page-sequence master-reference="A4">
				 <fo:flow flow-name="xsl-region-body">
				 	
				 	<!-- Iteriere über alle Jahre -->
				 	<xsl:for-each select="root/currentYear">
				 	
				 		<!-- Überschrift -->
				 		<fo:block font-family="Arial, sans-serif" font-size="15pt" font-weight="bold" margin-top="20pt">
							<fo:inline text-decoration="underline">
								Monatliche Arbeitstage in <xsl:value-of select="year"/>
							</fo:inline>
						</fo:block>
						
						<!-- Tabelle mit Arbeitstagen -->
						<fo:table font-family="Arial, sans-serif" width="100%"
							margin-top="5pt" table-layout="fixed">
							
							<fo:table-column column-width="65%"/>
							<fo:table-column column-width="35%"/>
							
							<!-- Tabellen-Header (Spaltenüberschriften) -->
							<fo:table-header>
								<fo:table-row text-align="center" font-size="14pt" font-weight="bold">
									<fo:table-cell border="solid black 1pt" padding="3pt">
										<fo:block>
											Monat
										</fo:block>
									</fo:table-cell>
									<fo:table-cell border="solid black 1pt" padding="3pt">
										<fo:block>
											Anzahl der Arbeitstage
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-header>
							
							<!-- Tabellen-Footer (Gesamtanzahl pro Jahr) -->
							<fo:table-footer>
								<fo:table-row>
									<fo:table-cell font-size="14pt" padding="3pt">
										<fo:block>
											Gesamte Anzahl der Arbeitstage
										</fo:block>
									</fo:table-cell>
									<fo:table-cell text-align="center" font-size="14pt" padding="3pt">
										<fo:block>
											<!-- Berechne Gesamtanzahl der Arbeitstage eines Jahres -->
											<xsl:value-of select="sum(month/numberOfWorkingDays)"/>
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-footer>
							
							<!-- Tabellen-Körper (alle Monate mit jeweiliger Anzahl) -->
							<fo:table-body>
							
								<!-- Iteriere über alle Monate eines Jahres -->
								<xsl:for-each select="month">
									<fo:table-row>
										<fo:table-cell font-size="14pt" border="solid black 1pt" padding="3pt">
											<fo:block>
												<xsl:value-of select="monthName"/>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="center" font-size="14pt" border="solid black 1pt" padding="3pt">
											<fo:block>
												<xsl:value-of select="numberOfWorkingDays"/>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
								</xsl:for-each>
							</fo:table-body>
						</fo:table>
				 	</xsl:for-each>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
</xsl:stylesheet>