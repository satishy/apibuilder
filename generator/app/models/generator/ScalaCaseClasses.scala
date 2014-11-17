package generator

import com.gilt.apidocgenerator.models.ServiceDescription
import core._
import lib.Text._

object ScalaCaseClasses extends CodeGenerator {
  def generate(sd: ServiceDescription): String = {
    generate(new ScalaServiceDescription(sd))
  }

  def generate(ssd: ScalaServiceDescription, genEnums: Seq[ScalaEnum] => String = generatePlayEnums): String = {
    Seq(
      s"package ${ssd.modelPackageName} {",
      ssd.models.map { model =>
        model.description.map { desc => ScalaUtil.textToComment(desc) + "\n" }.getOrElse("") +
        s"case class ${model.name}(${model.argList.getOrElse("")})"
      }.mkString("\n\n").indent(2),
      "",
      genEnums(ssd.enums).indent(2),
      s"}"
    ).mkString("\n")
  }

  private def generatePlayEnums(enums: Seq[ScalaEnum]): String = {
    enums.map { enum =>
      ScalaEnums.build(enum)
    }.mkString("\n\n")
  }
}